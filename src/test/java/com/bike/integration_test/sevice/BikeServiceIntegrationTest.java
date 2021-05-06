package com.bike.integration_test.sevice;

import com.bike.model.Bike;
import com.bike.service.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class BikeServiceIntegrationTest {

    @Autowired
    BikeService service;

    @Test
    void shouldSaveBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = service.addNewBike(bike1);
        assertNotNull(saved.getId());
        assertEquals(saved.getMake(), bike1.getMake());
        assertEquals(saved.getModel(), bike1.getModel());
        assertEquals(saved.getWorth(), bike1.getWorth());
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldSaveAndLoadBike() {
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike saved = service.addNewBike(bike1);
        Bike loaded = service.getById(saved.getId());
        assertNotNull(saved.getId());
        assertEquals(loaded.getMake(), bike1.getMake());
        assertEquals(loaded.getModel(), bike1.getModel());
        assertEquals(loaded.getWorth(), bike1.getWorth());
    }
}
