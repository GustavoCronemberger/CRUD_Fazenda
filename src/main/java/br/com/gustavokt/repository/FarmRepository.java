package br.com.gustavokt.repository;

import br.com.gustavokt.conn.ConnectionFactory;
import br.com.gustavokt.domain.Farm;
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
public class FarmRepository {
    public static List<Farm> findByName(String name) {
        log.info("Finding Farms by Name '{}'", name);
        List<Farm> farms = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producer producer = Producer.builder()
                        .name(rs.getString("producer_name"))
                        .id(rs.getInt("producer_id"))
                        .build();
                Farm farm = Farm.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .values(rs.getInt("values"))
                        .producer(producer)
                        .build();
                farms.add(farm);
            }

        } catch (SQLException e) {
            log.error("Error while not founding, trying to insert", e);
        }
        return farms;
    }

    private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = """
        SELECT a.id, a.name, a.values, a.producer_id, p.name as 'producer_name' From farm_catalog.farm a inner join
        farm_catalog.producer p on a.producer_id = p.id
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
            log.info("Deleted Farm '{}' from the database", id);
        } catch (SQLException e) {
            log.error("Error while trying to delete Farm '{}'", id, e);
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM `farm_catalog`.`farm` WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void save (Farm farm){
        log.info("Saving Farm '{}'", farm);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSave(conn, farm)) {
            ps.execute();
        }catch (SQLException e){
            log.error("Error while trying to save Farm '{}'", farm.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementSave (Connection conn, Farm farm) throws SQLException {
        String sql = "INSERT INTO `farm_catalog`.`farm` (`name`, `values`, `producer_id`) VALUES (?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, farm.getName());
        ps.setInt(2, farm.getValues());
        ps.setInt(3, farm.getId());
        return ps;
    }

    public static Optional<Farm> findById(Integer id) {
        log.info("Finding farms by Id '{}'", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return Optional.empty();
            Producer producer = Producer.builder()
                    .name(rs.getString("producer_name"))
                    .id(rs.getInt("producer_id"))
                    .build();
            Farm farm = Farm.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .values(rs.getInt("values"))
                    .producer(producer)
                    .build();
            return Optional.of(farm);
        } catch (SQLException e) {
            log.error("Error while not founding, trying to find all farms", e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementFindById (Connection conn, Integer id) throws SQLException {
        String sql = """
        SELECT a.id, a.name, a.values, a.producer_id, p.name as 'producer_name' From farm_catalog.farm a inner join
        farm_catalog.producer p on a.producer_id = p.id
        where a.id = ?;
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public static void update (Farm farm){
        log.info("Updating Farm '{}'", farm);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, farm)) {
            ps.execute();
            log.info("Updated Farm '{}'", farm.getId());
        }catch (SQLException e){
            log.error("Error while trying to update Farm '{}'", farm.getId(), e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate (Connection conn, Farm farm) throws SQLException {
        String sql = "UPDATE `farm_catalog`.`farm` SET `name` = ?, `values` = ? WHERE (`id` = ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, farm.getName());
        ps.setInt(2, farm.getValues());
        ps.setInt(3, farm.getId());
        return ps;
    }
}
