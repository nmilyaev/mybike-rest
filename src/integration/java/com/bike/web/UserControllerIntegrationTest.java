package com.bike.web;

import com.bike.dto.UserDto;
import com.bike.model.User;
import com.bike.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.UUID;

import static com.bike.util.IntegrationTestUtil.createUserRequest;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    @SneakyThrows
    void shouldCreateUser() {
        HttpEntity<String> request = createUserRequest(userDto);
        ResponseEntity<UserDto> response =
                restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create",
                        request,
                        UserDto.class);
        assertEquals(OK, response.getStatusCode());
        UserDto restUser = requireNonNull(response.getBody());
        user.setId(requireNonNull(restUser).getId());
        assertNotNull(restUser.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(user);
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() {
        HttpEntity<String> request = createUserRequest(userDto);
        // First creation should succeed
        ResponseEntity<UserDto> firstResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create", request,
                UserDto.class);
        assertEquals(OK, firstResponse.getStatusCode());
        assertNotNull(firstResponse.getBody().getId());

        // Second creation with the same email should fail with CONFLICT (409)
        ResponseEntity<UserDto> secondResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create", request,
                UserDto.class);
        assertEquals(BAD_REQUEST, secondResponse.getStatusCode());
    }

    @Test
    void getUserList() {
        service.createUser(user);
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity("http://localhost:" + serverPort + "/user", UserDto[].class);
        var restUsers = response.getBody();
        assertEquals(1, requireNonNull(restUsers).length);
        UserDto restUser = restUsers[0];
        assertThat(restUser)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(user);
    }

    @Test
    void shouldGetUser() {
        service.createUser(user);
        User restUser = restTemplate.getForObject("http://localhost:" + serverPort + "/user/" + user.getId(),
                User.class);
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
        User restUser = service.getById(user.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}