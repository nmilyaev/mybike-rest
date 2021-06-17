package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BikeServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    private BikeService service;

    @Autowired
    private UserService userService;

    private MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userService.createUser(user);
    }

    @Test
    void shouldSaveBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = service.addNewBike(bike);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldSaveAndLoadBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = service.addNewBike(bike);
        Bike loaded = service.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldDeleteBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        service.addNewBike(bike);
        assertNotNull(bike.getId());
        boolean success = service.deleteBike(bike.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(bike.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + bike.getId(), exception.getMessage());
    }

    @Test
    void shouldNotDeleteBikeByWrongId() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        service.addNewBike(bike);
        boolean success = service.deleteBike(UUID.randomUUID());
        assertFalse(success);
        Bike loaded = service.getById(bike.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }
}
