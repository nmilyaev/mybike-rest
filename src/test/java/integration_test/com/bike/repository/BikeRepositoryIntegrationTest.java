package integration_test.com.bike.repository;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BikeRepositoryIntegrationTest {
    @Autowired
    BikeRepository repository;

    @Test
    void shouldSaveBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = repository.save(bike1);
        assertNotNull(saved.getId());
        assertEquals(saved.getMake(), bike1.getMake());
        assertEquals(saved.getModel(), bike1.getModel());
        assertEquals(saved.getWorth(), bike1.getWorth());
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
    void shouldSaveAndLoadBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = repository.save(bike1);
        Bike loaded = repository.getOne(saved.getId());
        assertNotNull(saved.getId());
        assertEquals(loaded.getMake(), bike1.getMake());
        assertEquals(loaded.getModel(), bike1.getModel());
        assertEquals(loaded.getWorth(), bike1.getWorth());
    }

    @Test
    @Transactional
    void shouldDeleteBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = repository.save(bike1);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getOne(saved.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + saved.getId(), exception.getCause().getMessage());
    }
}
