package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BikeServiceIntegrationTest {

    @Autowired
    private BikeService service;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BikeRepository bikeRepository;

    private MybikeUser user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
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
    @Transactional
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
        boolean success = service.deleteBike(bike.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(bike.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + bike.getId(), exception.getMessage());
        Set<Bike> bikeOffers = user.getBikeOffers();
        // TODO make sure this test works - and add similar check to SaveBike()
        assertThat(!bikeOffers.contains(bike));
    }

    @Test
    @Transactional
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
