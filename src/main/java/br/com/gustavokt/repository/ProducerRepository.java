package br.com.gustavokt.repository;

import br.com.gustavokt.conn.ConnectionFactory;
import br.com.gustavokt.domain.Producer;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log4j2
public class ProducerRepository {
    public static List<Producer> findByName(String name) {
        log.info("Finding Producers by Name '{}'", name);
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producer producer = Producer.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
                producers.add(producer);
            }

        } catch (SQLException e) {
            log.error("Error while not founding, trying to insert", e);
        }
        return producers;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT * FROM farm_catalog.producer where name like ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static void delete(int id) {
        log.info("Deleting producer '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
            ps.execute();
            log.info("Deleted producer '{}' from the database", id);
        } catch (SQLException e) {
            log.error("Error while trying to delete producer '{}'", id, e);
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM farm_catalog.producer WHERE id = ? AND id NOT IN (SELECT producer_id FROM farm_catalog.farm);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }


    public static void save(Producer producer) {
        log.info("Saving producer '{}'", producer);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSave(conn, producer)) {

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating producer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Field idField = Producer.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(producer, generatedKeys.getInt(1));
                    log.info("Producer saved with ID '{}'", producer.getId());
                } else {
                    throw new SQLException("Creating producer failed, no ID obtained.");
                }
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            log.error("Error while trying to save Producer '{}'", producer.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementSave(Connection conn, Producer producer) throws SQLException {
        String sql = "INSERT INTO farm_catalog.producer (name) VALUES (?);";
        PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, producer.getName());
        return ps;
    }

    public static Optional<Producer> findById(Integer id) {
        log.info("Finding Producers by Id '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return Optional.empty();
            return Optional.of(Producer.builder().id(rs.getInt("id")).name(rs.getString("name")).build());
        } catch (SQLException e) {
            log.error("Error while not founding, trying to find all producers", e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementFindById (Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM farm_catalog.producer where id = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void update (Producer producer){
        log.info("Updating producer '{}'", producer);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, producer)) {
            ps.execute();
            log.info("Updated producer '{}'", producer.getId());
        }catch (SQLException e){
            log.error("Error while trying to update producer '{}'", producer.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate (Connection conn, Producer producer) throws SQLException {
        String sql = "UPDATE farm_catalog.producer SET name = ? WHERE (id = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, producer.getName());
        ps.setInt(2, producer.getId());
        return ps;
    }
}
