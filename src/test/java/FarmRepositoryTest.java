import br.com.gustavokt.conn.ConnectionFactory;
import br.com.gustavokt.domain.Farm;
import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.FarmRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FarmRepositoryTest {

    private Connection connection;
    private FarmRepository farmRepository;

    @BeforeEach
    public void setUp() {
        connection = ConnectionFactory.getConnection();
        farmRepository = new FarmRepository();
        cleanDatabase();
        ensureTestProducer();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    private void cleanDatabase() {
        // Clean the database before each test
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM farm_catalog.farm")) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureTestProducer() {
        // Check if the test producer exists, and insert it if not
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM farm_catalog.producer WHERE id = 1")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0) {
                try (PreparedStatement insertPs = connection.prepareStatement("INSERT INTO farm_catalog.producer (id, name) VALUES (1, 'Test Producer')")) {
                    insertPs.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByName() {
        Farm farm = Farm.builder().name("Test Farm").values(1000).producer(new Producer(1, "Test Producer")).build();
        farmRepository.save(farm);

        List<Farm> farms = farmRepository.findByName("Test Farm");
        assertFalse(farms.isEmpty(), "The farm should be found by name");
        assertEquals("Test Farm", farms.get(0).getName(), "The farm name should match");
    }

    @Test
    public void testDelete() {
        Farm farm = Farm.builder().name("Test Farm").values(1000).producer(new Producer(1, "Test Producer")).build();
        farmRepository.save(farm);

        assertNotNull(farm.getId(), "The farm ID should not be null after saving");
        farmRepository.delete(farm.getId());

        Optional<Farm> deletedFarm = farmRepository.findById(farm.getId());
        assertFalse(deletedFarm.isPresent(), "The farm should be correctly deleted");
    }

    @Test
    public void testSave() {
        Farm farm = Farm.builder().name("New Farm").values(2000).producer(new Producer(1, "Test Producer")).build();

        farmRepository.save(farm);
        assertNotNull(farm.getId(), "The farm ID should not be null after saving");

        Optional<Farm> savedFarm = farmRepository.findById(farm.getId());
        assertTrue(savedFarm.isPresent(), "The farm should be found after saving");
        assertEquals("New Farm", savedFarm.get().getName(), "The farm name should match after saving");
    }

    @Test
    public void testFindById() {
        Farm farm = Farm.builder().name("Test Farm ID").values(3000).producer(new Producer(1, "Test Producer")).build();
        farmRepository.save(farm);

        Optional<Farm> foundFarm = farmRepository.findById(farm.getId());
        assertTrue(foundFarm.isPresent(), "The farm should be found by ID");
        assertEquals("Test Farm ID", foundFarm.get().getName(), "The farm name should match by ID");
    }

    @Test
    public void testUpdate() {
        Farm farm = Farm.builder().name("Update Farm").values(4000).producer(new Producer(1, "Test Producer")).build();
        farmRepository.save(farm);

        farm = Farm.builder().id(farm.getId()).name("Updated Farm").values(5000).producer(farm.getProducer()).build();
        farmRepository.update(farm);

        Optional<Farm> updatedFarm = farmRepository.findById(farm.getId());
        assertTrue(updatedFarm.isPresent(), "The farm should be found after update");
        assertEquals("Updated Farm", updatedFarm.get().getName(), "The farm name should be updated");
        assertEquals(5000, updatedFarm.get().getValues(), "The farm values should be updated");
    }
}
