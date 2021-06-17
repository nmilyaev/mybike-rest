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
public class MybikeUserServiceIntegrationTest extends BasicServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

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
        boolean success = userService.deleteUser(user.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> userService.getById(user.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
    }

    @Test
    @Transactional
    void shouldNotDeleteUserByWrongId() {
        userService.createUser(user);
        boolean success = userService.deleteUser(UUID.randomUUID());
        assertFalse(success);
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
        assertEquals("User with this email already exists: " + saved.getId(), exception.getMessage());
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
    void shouldReturnAllBikesForUser() {
        MybikeUser saved = userService.createUser(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
        List<Bike> userBikes = userService.getUserBikes(saved);
        assertEquals(1, userBikes.size());
        assertThat(userBikes.get(0))
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }
}
