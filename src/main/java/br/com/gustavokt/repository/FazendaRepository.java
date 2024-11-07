package br.com.gustavokt.repository;

import br.com.gustavokt.conn.ConnectionFactory;
import br.com.gustavokt.domain.Fazenda;
import br.com.gustavokt.domain.Producer;
import lombok.extern.log4j.Log4j2;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class FazendaRepository {
    public static List<Fazenda> findByName(String name) {
        log.info("Finding Fazendas by Name '{}'", name);
        List<Fazenda> fazendas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producer producer = Producer.builder()
                        .name(rs.getString("producer_name"))
                        .id(rs.getInt("producer_id"))
                        .build();
                Fazenda fazenda = Fazenda.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .values(rs.getInt("values"))
                        .producer(producer)
                        .build();
                fazendas.add(fazenda);
            }

        } catch (SQLException e) {
            log.error("Error while not founding, trying to insert", e);
        }
        return fazendas;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = """
        SELECT a.id, a.name, a.values, a.producer_id, p.name as 'producer_name' From fazenda_catalog.fazenda a inner join
        fazenda_catalog.producer p on a.producer_id = p.id
        where a.name like ?;
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static void delete(int id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(conn, id)) {
            ps.execute();
            log.info("Deleted Fazenda '{}' from the database", id);
        } catch (SQLException e) {
            log.error("Error while trying to delete Fazenda '{}'", id, e);
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM `fazenda_catalog`.`fazenda` WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void save (Fazenda fazenda){
        log.info("Saving Fazenda '{}'", fazenda);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSave(conn, fazenda)) {
            ps.execute();
        }catch (SQLException e){
            log.error("Error while trying to save Fazenda '{}'", fazenda.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementSave (Connection conn, Fazenda fazenda) throws SQLException {
        String sql = "INSERT INTO `fazenda_catalog`.`fazenda` (`name`, `values`, `producer_id`) VALUES (?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, fazenda.getName());
        ps.setInt(2, fazenda.getValues());
        ps.setInt(3, fazenda.getId());
        return ps;
    }

    public static Optional<Fazenda> findById(Integer id) {
        log.info("Finding fazendas by Id '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return Optional.empty();
            Producer producer = Producer.builder()
                    .name(rs.getString("producer_name"))
                    .id(rs.getInt("producer_id"))
                    .build();
            Fazenda fazenda = Fazenda.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .values(rs.getInt("values"))
                    .producer(producer)
                    .build();
            return Optional.of(fazenda);
        } catch (SQLException e) {
            log.error("Error while not founding, trying to find all fazendas", e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementFindById (Connection conn, Integer id) throws SQLException {
        String sql = """
        SELECT a.id, a.name, a.values, a.producer_id, p.name as 'producer_name' From fazenda_catalog.fazenda a inner join
        fazenda_catalog.producer p on a.producer_id = p.id
        where a.id = ?;
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void update (Fazenda fazenda){
        log.info("Updating Fazenda '{}'", fazenda);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, fazenda)) {
            ps.execute();
            log.info("Updated Fazenda '{}'", fazenda.getId());
        }catch (SQLException e){
            log.error("Error while trying to update Fazenda '{}'", fazenda.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate (Connection conn, Fazenda fazenda) throws SQLException {
        String sql = "UPDATE `fazenda_catalog`.`fazenda` SET `name` = ?, `values` = ? WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, fazenda.getName());
        ps.setInt(2, fazenda.getValues());
        ps.setInt(3, fazenda.getId());
        return ps;
    }
}
