package com.bike.web;

import com.bike.dto.MybikeUserDto;
import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private UserService service;

    @Test
    void shouldCreateUser() {
        ResponseEntity<MybikeUserDto> response = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create", userDto,
                MybikeUserDto.class);
        assertEquals(OK, response.getStatusCode());
        MybikeUserDto restUser = response.getBody();
        user.setId(Objects.requireNonNull(restUser).getId());
        assertNotNull(restUser.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldNotCreateUserWithDuplicateEmail() {
        // First creation should succeed
        ResponseEntity<MybikeUserDto> firstResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create", userDto,
                MybikeUserDto.class);
        assertEquals(OK, firstResponse.getStatusCode());
        assertNotNull(firstResponse.getBody().getId());

        // Second creation with the same email should fail with CONFLICT (409)
        ResponseEntity<MybikeUserDto> secondResponse = restTemplate.postForEntity("http://localhost:" + serverPort + "/user/create", userDto,
                MybikeUserDto.class);
        assertEquals(BAD_REQUEST, secondResponse.getStatusCode());
    }

    @Test
    void getUserList() {
        service.createUser(user);
        ResponseEntity<MybikeUserDto[]> response = restTemplate.getForEntity("http://localhost:" + serverPort + "/user", MybikeUserDto[].class);
        var restUsers = response.getBody();
        System.out.println(restUsers);
//        assertEquals(1, response.length);
//        MybikeUser restUser = response[0];
//        assertThat(restUser)
//                .usingRecursiveComparison()
//                .isEqualTo(user);
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