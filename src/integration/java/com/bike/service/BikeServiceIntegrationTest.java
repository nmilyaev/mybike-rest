package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.User;
import com.bike.repository.BikeHireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.bike.util.IntegrationTestUtil.aMybikeUser;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BikeServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BikeHireService bikeHireService;

    @Autowired
    private BikeHireRepository bikeHireRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = aMybikeUser();
        userService.createUser(user);
    }

    @Test
    void shouldSaveBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = bikeService.addNewBike(bike);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldSaveAndLoadBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = bikeService.addNewBike(bike);
        Bike loaded = bikeService.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldDeleteBike() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
        assertNotNull(bike.getId());
        bikeService.deleteBike(bike.getId());
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> bikeService.getById(bike.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + bike.getId(), exception.getMessage());
    }

    @Test
    void shouldNotDeleteBikeByWrongId() {
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
        Bike loaded = bikeService.getById(bike.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldReturnAllHiresForBike() {
        LocalDate now = now();
        User borrower = aMybikeUser("Paul", "Smith", "p.s@mail.com", "5 Brockwell park", "SW8 1NR", "London", "07777777777", "password");
        userService.createUser(borrower);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
        BikeHire hire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        bikeHireService.saveHire(hire);
        List<BikeHire> hiresForBike = bikeService.getHiresForBike(bike);
        assertEquals(1, hiresForBike.size());
        assertThat(hiresForBike.getFirst())
                .usingRecursiveComparison()
                .isEqualTo(hire);
    }

    @Test
    void shouldReturnAllFutureHiresForBike() {
        LocalDate now = now();
        User borrower = aMybikeUser("Paul", "Smith", "p.s@mail.com", "5 Brockwell park", "SW8 1NR", "London", "07777777777", "password");
        userService.createUser(borrower);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
        BikeHire hire1 = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .build();
        bikeHireRepository.save(hire1);
        BikeHire hire2 = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        bikeHireService.saveHire(hire2);
        List<BikeHire> allHires = bikeHireService.getAllHires();
        assertEquals(2, allHires.size());
        List<BikeHire> hiresForBike = bikeService.getFutureHiresForBike(bike);
        assertEquals(1, hiresForBike.size());
        assertThat(hiresForBike.getFirst())
                .usingRecursiveComparison()
                .isEqualTo(hire2);
    }
}
