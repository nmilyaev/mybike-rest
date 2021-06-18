package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeHireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BikeServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BikeHireService bikeHireService;

    @Autowired
    private BikeHireRepository bikeHireRepository;

    private Clock clock;

    private MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
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
        UUID randId = UUID.randomUUID();
        Throwable exception = assertThrows(EmptyResultDataAccessException.class, () -> bikeService.deleteBike(randId));
        assertEquals("No class com.bike.model.Bike entity with id " + randId + " exists!", exception.getMessage());
        Bike loaded = bikeService.getById(bike.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldReturnAllHiresForBike(){
        LocalDate now = now();
        MybikeUser borrower = MybikeUser.createWithRequiredFields("Paul", "Smith", "p.s@mail.com", "SW8 1NR", "password");
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
        assertThat(hiresForBike.get(0))
                .usingRecursiveComparison()
                .isEqualTo(hire);
    }

    @Test
    void shouldReturnAllFutureHiresForBike(){
        LocalDate now = now();
        MybikeUser borrower = MybikeUser.createWithRequiredFields("Paul", "Smith", "p.s@mail.com", "SW8 1NR", "password");
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
        assertThat(hiresForBike.get(0))
                .usingRecursiveComparison()
                .isEqualTo(hire2);
    }
}
