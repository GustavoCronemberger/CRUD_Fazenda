import br.com.gustavokt.conn.ConnectionFactory;
import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.ProducerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerRepositoryTest {

    private Connection connection;
    private ProducerRepository producerRepository;

    @BeforeEach
    public void setUp() {
        connection = ConnectionFactory.getConnection();
        producerRepository = new ProducerRepository();
        cleanDatabase();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    private void cleanDatabase() {
        try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM farm_catalog.farm");
             PreparedStatement ps2 = connection.prepareStatement("DELETE FROM farm_catalog.producer")) {
            ps1.execute();
            ps2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByName() {
        Producer producer = Producer.builder().name("Test Producer").build();
        producerRepository.save(producer);

        List<Producer> producers = producerRepository.findByName("Test Producer");
        assertFalse(producers.isEmpty(), "The producer should be found by name");
        assertEquals("Test Producer", producers.get(0).getName(), "The producer name should match");
    }

    @Test
    public void testDelete() {
        Producer producer = Producer.builder().name("Test Producer").build();
        producerRepository.save(producer);

        assertNotNull(producer.getId(), "The producer ID should not be null after saving");
        producerRepository.delete(producer.getId());

        Optional<Producer> deletedProducer = producerRepository.findById(producer.getId());
        assertFalse(deletedProducer.isPresent(), "The producer should be correctly deleted");
    }

    @Test
    public void testSave() {
        Producer producer = Producer.builder().name("New Producer").build();

        producerRepository.save(producer);
        assertNotNull(producer.getId(), "The producer ID should not be null after saving");

        Optional<Producer> savedProducer = producerRepository.findById(producer.getId());
        assertTrue(savedProducer.isPresent(), "The producer should be found after saving");
        assertEquals("New Producer", savedProducer.get().getName(), "The producer name should match after saving");
    }

    @Test
    public void testFindById() {
        Producer producer = Producer.builder().name("Test Producer ID").build();
        producerRepository.save(producer);

        Optional<Producer> foundProducer = producerRepository.findById(producer.getId());
        assertTrue(foundProducer.isPresent(), "The producer should be found by ID");
        assertEquals("Test Producer ID", foundProducer.get().getName(), "The producer name should match by ID");
    }

    @Test
    public void testUpdate() {
        Producer producer = Producer.builder().name("Update Producer").build();
        producerRepository.save(producer);

        producer = Producer.builder().id(producer.getId()).name("Updated Producer").build();
        producerRepository.update(producer);

        Optional<Producer> updatedProducer = producerRepository.findById(producer.getId());
        assertTrue(updatedProducer.isPresent(), "The producer should be found after update");
        assertEquals("Updated Producer", updatedProducer.get().getName(), "The producer name should be updated");
    }
}

