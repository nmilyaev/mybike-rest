package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.BikeHire;
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
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BikeHireServiceIntegrationTest {

    @Autowired
    BikeHireService service;

    @Autowired
    BikeService bikeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private UserService userService;

    private MybikeUser owner;
    private MybikeUser borrower;
    private Bike bike;
    private LocalDate now;

    @BeforeEach
    void setUp() {
        bikeRepository.deleteAll();
        userRepository.deleteAll();
        owner = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userService.createUser(owner);
        borrower = MybikeUser.createWithRequiredFields("Paul", "Smith", "p.s@mail.com", "SW8 1NR", "password");
        userService.createUser(borrower);
        bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), owner);
        bikeService.addNewBike(bike);
        now = now();
    }

    @Test
    @Transactional
    void shouldCreateBikeHire() {
        BikeHire hire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .firstDay(now)
                .lastDay(now.plusDays(2))
                .build();
        BikeHire saved = service.createHire(hire);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(hire);
    }

    @Test
    @Transactional
    void shouldCancelBikeHire() {
        BikeHire hire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .firstDay(now)
                .lastDay(now.plusDays(2))
                .build();
        BikeHire saved = service.createHire(hire);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(hire);
        boolean success = service.cancelHire(saved.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(saved.getId()));
        assertEquals("Unable to find com.bike.model.BikeHire with id " + saved.getId(), exception.getMessage());
    }
}
