package com.bike.web;

import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    void shouldCreateUser() {
        ResponseEntity<MybikeUser> response = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/createUser", user,
                MybikeUser.class);
        assertEquals(OK, response.getStatusCode());
        MybikeUser restUser = response.getBody();
        user.setId(Objects.requireNonNull(restUser).getId());
        assertNotNull(restUser.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() {
        // First creation should succeed
        ResponseEntity<MybikeUser> firstResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/createUser", user,
                MybikeUser.class);
        assertEquals(OK, firstResponse.getStatusCode());
        assertNotNull(firstResponse.getBody().getId());

        // Second creation with the same email should fail with CONFLICT (409)
        ResponseEntity<MybikeUser> secondResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/createUser", user,
                MybikeUser.class);
        assertEquals(HttpStatus.CONFLICT, secondResponse.getStatusCode());
    }

    @Test
    void getUserList() {
        service.createUser(user);
        MybikeUser[] restUsers = restTemplate.getForObject("http://localhost:" + serverPort + "/user/", MybikeUser[].class);
        assertEquals(1, restUsers.length);
        MybikeUser restUser = restUsers[0];
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldGetUser() {
        service.createUser(user);
        MybikeUser restUser = restTemplate.getForObject("http://localhost:" + serverPort + "/user/" + user.getId(),
                MybikeUser.class);
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldDeleteUser() {
        service.createUser(user);
        restTemplate.delete(URI.create("http://localhost:" + serverPort + "/user/" + user.getId()));
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(user.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
    }

    @Test
    void shouldNotDeleteNonexistingUser() {
        service.createUser(user);
        restTemplate.delete(URI.create("http://localhost:" + serverPort + "/user/" + UUID.randomUUID()));
        MybikeUser restUser = service.getById(user.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}