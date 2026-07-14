package com.bike.service;

import static com.bike.util.IntegrationTestUtil.aMybikeUser;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.User;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserServiceIntegrationTest extends BasicServiceIntegrationTest {

  @Autowired private UserService userService;

  @Autowired private BikeService bikeService;

  @Autowired private BikeHireService bikeHireService;

  private User user;

  @BeforeEach
  void setUp() {
    user = aMybikeUser();
  }

  @Test
  void shouldSaveUser() {
    var saved = userService.createUser(user);
    assertNotNull(saved.getId());
    assertThat(saved).usingRecursiveComparison().isEqualTo(user);
    log.info("............UUID: {}", saved.getId());
  }

  @Test
  void shouldSaveAndLoadUser() {
    var saved = userService.createUser(user);
    var loaded = userService.getById(saved.getId());
    assertNotNull(saved.getId());
    assertThat(loaded).usingRecursiveComparison().isEqualTo(user);
  }

  @Test
  void shouldDeleteUser() {
    userService.createUser(user);
    userService.deleteUser(user.getId());
    Throwable exception =
        assertThrows(EntityNotFoundException.class, () -> userService.getById(user.getId()));
    assertEquals(
        "Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
  }

  @Test
  void shouldNotDeleteUserByWrongId() {
    userService.createUser(user);
    var loaded = userService.getById(user.getId());
    assertNotNull(loaded.getId());
    assertThat(loaded).usingRecursiveComparison().isEqualTo(user);
  }

  @Test
  void shouldNotCreateDuplicateUser() {
    var saved = userService.createUser(user);
    assertNotNull(saved.getId());
    user = aMybikeUser();
    assertNull(user.getId());
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    assertEquals(
        "User with this email already exists: " + saved.getEmail(), exception.getMessage());
  }

  @Test
  void shouldSaveAndFetchAllUsers() {
    var saved1 = userService.createUser(user);
    assertNotNull(saved1.getId());
    user =
        aMybikeUser(
            "Nick",
            "Mills",
            "n.n@mail.com",
            "10 Kyiv avenue",
            "SW9 1NR",
            "London",
            "07777777777",
            "password");
    user.setEmail("m.n@mail.com");
    assertNull(user.getId());
    User saved2 = userService.createUser(user);
    assertNotNull(saved2.getId());
    List<User> allUsers = userService.getAll();
    assertEquals(2, allUsers.size());
    assertThat(allUsers.get(0)).usingRecursiveComparison().isEqualTo(saved1);
    assertThat(allUsers.get(1)).usingRecursiveComparison().isEqualTo(saved2);
  }

  @Test
  void shouldReturnAllBikesOwnerByUser() {
    User saved = userService.createUser(user);
    var bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
    var bike2 = new Bike("Dawes", "Galaxy", BigDecimal.valueOf(120.00), user);
    bikeService.addNewBike(bike1);
    bikeService.addNewBike(bike2);
    List<Bike> userBikes = userService.getUserBikes(saved);
    assertEquals(2, userBikes.size());
    assertThat(userBikes.get(0)).usingRecursiveComparison().isEqualTo(bike1);
    assertThat(userBikes.get(1)).usingRecursiveComparison().isEqualTo(bike2);
  }

  @Test
  void shouldReturnAllHiresForUser() {
    LocalDate now = now();
    var owner = aMybikeUser();
    userService.createUser(owner);
    var borrower =
        aMybikeUser(
            "Paul",
            "Smith",
            "p.s@mail.com",
            "5 Brockwell park",
            "SW8 1NR",
            "London",
            "07777777777",
            "password");
    userService.createUser(borrower);
    var bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), owner);
    bikeService.addNewBike(bike);
    BikeHire hire =
        BikeHire.builder()
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
    assertThat(userHires.getFirst()).usingRecursiveComparison().isEqualTo(hire);
  }
}
