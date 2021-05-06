package integration_test.model;

import com.bike.repository.BikeRepository;
import com.bike.service.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@Slf4j
@DataJpaTest
public class BikeRepositoryIntegrationTest {
    @Autowired
    BikeRepository repository;
    @Autowired
    BikeService service;

    @Test
    void contextLoads() {
        log.info("............classpath: {}", System.getProperty("java.class.path"));
    }

    @Test
    void shouldSaveAndLoadBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike("Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        Bike savedBike = service.addNewBike(bike1);
        log.info("............UUID: {}", savedBike.getId());
    }
}
