package com.bike.web;

import static com.bike.util.IntegrationTestUtil.aMybikeUser;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

import com.bike.dto.BikeDto;
import com.bike.dto.UserDto;
import com.bike.model.Bike;
import com.bike.service.BikeService;
import com.bike.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

class BikeControllerIntegrationTest extends AbstractControllerIntegrationTest {

  @Autowired private BikeService service;

  @Autowired private UserService userService;

  private Bike bike;

  @BeforeEach
  void setUp() {
    user = aMybikeUser();
    userDto = UserDto.fromEntity(user);
    userService.createUser(user);
    bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
  }

  @Test
  void shouldCreateBike() {
    ResponseEntity<BikeDto> response =
        restTemplate.postForEntity(
            "http://localhost:" + serverPort + "/bike/createBike", bike, BikeDto.class);
    assertEquals(OK, response.getStatusCode());
    BikeDto restBike = response.getBody();
    bike.setId(requireNonNull(restBike).getId());
    assertNotNull(restBike.getId());
    assertThat(restBike)
        .usingRecursiveComparison()
        .ignoringFields("owner.password")
        .isEqualTo(bike);
  }

  @Test
  void shouldGetBike() {
    service.addNewBike(bike);
    ResponseEntity<BikeDto> response =
        restTemplate.getForEntity(
            "http://localhost:" + serverPort + "/bike/" + bike.getId(), BikeDto.class);
    BikeDto restBike = response.getBody();
    assertThat(restBike)
        .usingRecursiveComparison()
        .ignoringFields("owner.password")
        .isEqualTo(bike);
  }

  @Test
  void getBikeList() {
    service.addNewBike(bike);
    ResponseEntity<BikeDto[]> response =
        restTemplate.getForEntity("http://localhost:" + serverPort + "/bike", BikeDto[].class);
    BikeDto[] restBikes = response.getBody();
    assertEquals(1, requireNonNull(restBikes).length);
    BikeDto restBike = restBikes[0];
    assertThat(restBike)
        .usingRecursiveComparison()
        .ignoringFields("owner.password")
        .isEqualTo(bike);
  }

  @Test
  void deleteBike() {
    service.addNewBike(bike);
    restTemplate.delete(URI.create("http://localhost:" + serverPort + "/bike/" + bike.getId()));
    Throwable exception =
        assertThrows(EntityNotFoundException.class, () -> service.getById(bike.getId()));
    assertEquals(
        "Unable to find com.bike.model.Bike with id " + bike.getId(), exception.getMessage());
  }
}
