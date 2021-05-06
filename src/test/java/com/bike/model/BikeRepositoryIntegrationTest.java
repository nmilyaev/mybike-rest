package com.bike.model;

import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
public class BikeRepositoryIntegrationTest {
    @Autowired
    BikeRepository repository;

    @Test
    void shouldSaveBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = repository.save(bike1);
        assertNotNull(saved.id);
        assertEquals(saved.getMake(), bike1.getMake());
        assertEquals(saved.getModel(), bike1.getModel());
        assertEquals(saved.getWorth(), bike1.getWorth());
        log.info("............UUID: {}", saved.id);
    }

    @Test
    void shouldSaveAndLoadBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = repository.save(bike1);
        Bike loaded = repository.getOne(saved.id);
        assertNotNull(saved.id);
        assertEquals(loaded.getMake(), bike1.getMake());
        assertEquals(loaded.getModel(), bike1.getModel());
        assertEquals(loaded.getWorth(), bike1.getWorth());
    }
}
