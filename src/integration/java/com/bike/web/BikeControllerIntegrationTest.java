package com.bike.web;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.service.BikeService;
import com.bike.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

class BikeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private BikeService service;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateBike() {
        userService.createUser(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        ResponseEntity<Bike> response = restTemplate.postForEntity("http://localhost:" + serverPort + "/bike/createBike", bike,
                Bike.class);
        assertEquals(OK, response.getStatusCode());
        Bike restBike = response.getBody();
        bike.setId(Objects.requireNonNull(restBike).getId());
        assertNotNull(restBike.getId());
        assertThat(restBike)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldGetBike() {
        userService.createUser(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        service.addNewBike(bike);
        Bike restBike = restTemplate.getForObject("http://localhost:" + serverPort + "/bike/" + bike.getId(),
                Bike.class);
        assertThat(restBike)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void getBikeList() {
        userService.createUser(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        service.addNewBike(bike);
        Bike[] restBikes = restTemplate.getForObject("http://localhost:" + serverPort + "/bike/", Bike[].class);
        assertEquals(1, restBikes.length);
        Bike restBike = restBikes[0];
        assertThat(restBike)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void deleteBike() {
        userService.createUser(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        service.addNewBike(bike);
        restTemplate.delete(URI.create("http://localhost:" + serverPort + "/bike/" + bike.getId()));
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(bike.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + bike.getId(), exception.getMessage());
    }
}