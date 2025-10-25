import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.repositories.LogisticsRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TestLogitic {
    @Autowired
    private LogisticsRepository logisticsRepository;

    @Test
    void testSaveAndFindLogistics() {
        // Create a new Logistics entity
        Logistics logistics = new Logistics();
        logistics.setDescription("Tables and chairs");
        logistics.setReserve(true);
        logistics.setPrixUnit(50.0f);
        logistics.setQuantite(100);

        // Save the entity
        Logistics savedLogistics = logisticsRepository.save(logistics);

        // Check it has an ID
        assertThat(savedLogistics.getIdLog()).isNotNull();

        // Retrieve it from the repository
        Optional<Logistics> retrieved = logisticsRepository.findById(savedLogistics.getIdLog());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getDescription()).isEqualTo("Tables and chairs");
        assertThat(retrieved.get().isReserve()).isTrue();
        assertThat(retrieved.get().getPrixUnit()).isEqualTo(50.0f);
        assertThat(retrieved.get().getQuantite()).isEqualTo(100);
    }

    @Test
    void testDeleteLogistics() {
        Logistics logistics = new Logistics();
        logistics.setDescription("Projectors");
        logistics.setReserve(false);
        logistics.setPrixUnit(200.0f);
        logistics.setQuantite(5);

        Logistics saved = logisticsRepository.save(logistics);

        logisticsRepository.deleteById(saved.getIdLog());

        Optional<Logistics> deleted = logisticsRepository.findById(saved.getIdLog());
        assertThat(deleted).isNotPresent();
    }

    @Test
    void testFindAllLogistics() {
        Logistics l1 = new Logistics(0, "Tables", true, 30.0f, 50);
        Logistics l2 = new Logistics(0, "Chairs", false, 10.0f, 200);

        logisticsRepository.save(l1);
        logisticsRepository.save(l2);

        assertThat(logisticsRepository.findAll().size()).isGreaterThanOrEqualTo(2);
    }
}
