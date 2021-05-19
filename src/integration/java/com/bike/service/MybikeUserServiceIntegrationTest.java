package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class MybikeUserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    private MybikeUser user;
    @Autowired
    BikeService bikeService;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    void shouldSaveUser() {
        MybikeUser saved = userService.addNewUser(user);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldSaveAndLoadUser() {
        MybikeUser saved = userService.addNewUser(user);
        MybikeUser loaded = userService.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldDeleteUser() {
        userService.addNewUser(user);
        boolean success = userService.deleteUser(user.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> userService.getById(user.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldNotDeleteUserByWrongId() {
        userService.addNewUser(user);
        boolean success = userService.deleteUser(UUID.randomUUID());
        assertFalse(success);
        MybikeUser loaded = userService.getById(user.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldSaveAndFetchAllUsers() {
        MybikeUser saved1 = userService.addNewUser(user);
        assertNotNull(saved1.getId());
        setUp();
        assertNull(user.getId());
        MybikeUser saved2 = userService.addNewUser(user);
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
    void shouldSaveBikeOffersForUser() {
        MybikeUser user = userService.addNewUser(this.user);
        assertNotNull(user.getId());
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        bike = userService.listNewBike(user, bike);
        assertNotNull(bike.getId());
        assertEquals(1, user.getBikeOffers().size());
        assertThat(user.getBikeOffers().iterator().next())
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }
}
