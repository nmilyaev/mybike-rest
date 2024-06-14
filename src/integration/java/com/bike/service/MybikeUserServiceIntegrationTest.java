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
import org.springframework.dao.EmptyResultDataAccessException;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class MybikeUserServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BikeHireService bikeHireService;

    private MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    void shouldSaveUser() {
        MybikeUser saved = userService.createUser(user);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    void shouldSaveAndLoadUser() {
        MybikeUser saved = userService.createUser(user);
        MybikeUser loaded = userService.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldDeleteUser() {
        userService.createUser(user);
        userService.deleteUser(user.getId());
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> userService.getById(user.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
    }

    @Test
    void shouldNotDeleteUserByWrongId() {
        userService.createUser(user);
        UUID randId = UUID.randomUUID();
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(randId));
        assertEquals("Unable to delete user with id " + randId, exception.getMessage());
        MybikeUser loaded = userService.getById(user.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldNotCreateDuplicateUser() {
        MybikeUser saved = userService.createUser(user);
        assertNotNull(saved.getId());
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        assertNull(user.getId());
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        assertEquals("User with this email already exists: " + saved.getEmail(), exception.getMessage());
    }

    @Test
    void shouldSaveAndFetchAllUsers() {
        MybikeUser saved1 = userService.createUser(user);
        assertNotNull(saved1.getId());
        user = MybikeUser.createWithRequiredFields("Nick", "Mills", "n.n@mail.com", "SW9 1NR", "password");
        user.setEmail("m.n@mail.com");
        assertNull(user.getId());
        MybikeUser saved2 = userService.createUser(user);
        assertNotNull(saved2.getId());
        List<MybikeUser> allUsers = userService.getAll();
        assertEquals(2, allUsers.size());
        assertThat(allUsers.get(0))
                .usingRecursiveComparison()
                .isEqualTo(saved1);
        assertThat(allUsers.get(1))
                .usingRecursiveComparison()
                .isEqualTo(saved2);
    }

    @Test
    void shouldReturnAllBikesOwnerByUser() {
        MybikeUser saved = userService.createUser(user);
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike bike2 = new Bike("Dawes", "Galaxy", BigDecimal.valueOf(120.00), user);
        bikeService.addNewBike(bike1);
        bikeService.addNewBike(bike2);
        List<Bike> userBikes = userService.getUserBikes(saved);
        assertEquals(2, userBikes.size());
        assertThat(userBikes.get(0))
                .usingRecursiveComparison()
                .isEqualTo(bike1);
        assertThat(userBikes.get(1))
                .usingRecursiveComparison()
                .isEqualTo(bike2);
    }

    @Test
    void shouldReturnAllHiresForUser(){
        LocalDate now = now();
        MybikeUser owner = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userService.createUser(owner);
        MybikeUser borrower = MybikeUser.createWithRequiredFields("Paul", "Smith", "p.s@mail.com", "SW8 1NR", "password");
        userService.createUser(borrower);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), owner);
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
        List<BikeHire> userHires = userService.getUserHires(borrower);
        assertEquals(1, userHires.size());
        assertThat(userHires.get(0))
                .usingRecursiveComparison()
                .isEqualTo(hire);
    }
}
