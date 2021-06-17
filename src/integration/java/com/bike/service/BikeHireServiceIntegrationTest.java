package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BikeHireServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    BikeHireService service;

    @Autowired
    BikeService bikeService;

    @Autowired
    private UserService userService;

    private MybikeUser owner;
    private MybikeUser borrower;
    private Bike bike;
    private LocalDate now;

    @BeforeEach
    void setUp() {
        now = now();
        owner = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        owner = userService.createUser(owner);
        borrower = MybikeUser.createWithRequiredFields("Paul", "Smith", "p.s@mail.com", "SW8 1NR", "password");
        userService.createUser(borrower);
        bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), owner);
        bikeService.addNewBike(bike);
    }

    @Test
    void shouldCreateBikeHire() {
        createAndVerifyBikeHire();
    }

    @Test
    void shouldCancelBikeHire() {
        BikeHire hire = createAndVerifyBikeHire();
        Long id = hire.getId();
        assertNotNull(id);
        service.cancelHire(id);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(id));
        assertEquals("Unable to find com.bike.model.BikeHire with id " + id, exception.getMessage());
    }

    @Test
    void shouldNotFindNonExistingBikeHire() {
        BikeHire hire = createAndVerifyBikeHire();
        Long id = hire.getId() + 1;
        assertNotNull(id);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(id));
        assertEquals("Unable to find com.bike.model.BikeHire with id " + id, exception.getMessage());
    }

    private BikeHire createAndVerifyBikeHire() {
        BikeHire hire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .firstDay(now)
                .lastDay(now.plusDays(2))
                .build();
        BikeHire saved = service.saveHire(hire);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(hire);
        return saved;
    }
}
