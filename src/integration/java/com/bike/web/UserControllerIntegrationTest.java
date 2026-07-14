package com.bike.web;

import static com.bike.util.IntegrationTestUtil.createUserRequest;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import com.bike.dto.UserDto;
import com.bike.model.User;
import com.bike.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTest {

  @Autowired private UserService service;

  @Test
  @SneakyThrows
  void shouldCreateUser() {
    HttpEntity<String> request = createUserRequest(userDto);
    ResponseEntity<UserDto> response =
        restTemplate.postForEntity(
            "http://localhost:" + serverPort + "/user/create", request, UserDto.class);
    assertEquals(CREATED, response.getStatusCode());
    UserDto restUser = requireNonNull(response.getBody());
    user.setId(requireNonNull(restUser).getId());
    assertNotNull(restUser.getId());
    assertThat(restUser).usingRecursiveComparison().ignoringFields("password").isEqualTo(user);
  }

  @Test
  void shouldNotCreateUserWithDuplicateEmail() {
    HttpEntity<String> request = createUserRequest(userDto);
    // First creation should succeed
    ResponseEntity<UserDto> firstResponse =
        restTemplate.postForEntity(
            "http://localhost:" + serverPort + "/user/create", request, UserDto.class);
    assertEquals(CREATED, firstResponse.getStatusCode());
    assertNotNull(firstResponse.getBody().getId());

    // Second creation with the same email should fail with CONFLICT (409)
    ResponseEntity<UserDto> secondResponse =
        restTemplate.postForEntity(
            "http://localhost:" + serverPort + "/user/create", request, UserDto.class);
    assertEquals(BAD_REQUEST, secondResponse.getStatusCode());
  }

  @Test
  void getUserList() {
    service.createUser(user);
    UserDto[] response =
        restTemplate.getForObject("http://localhost:" + serverPort + "/user", UserDto[].class);
    assertEquals(1, requireNonNull(response).length);
    UserDto restUser = response[0];
    assertThat(restUser).usingRecursiveComparison().ignoringFields("password").isEqualTo(user);
  }

  @Test
  void shouldGetUser() {
    service.createUser(user);
    UserDto restUser =
        restTemplate.getForObject(
            "http://localhost:" + serverPort + "/user/" + user.getId(), UserDto.class);
    assertThat(restUser).usingRecursiveComparison().ignoringFields("password").isEqualTo(user);
  }

  @Test
  void shouldDeleteUser() {
    service.createUser(user);
    restTemplate.delete(URI.create("http://localhost:" + serverPort + "/user/" + user.getId()));
    Throwable exception =
        assertThrows(EntityNotFoundException.class, () -> service.getById(user.getId()));
    assertEquals(
        "Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
  }

  @Test
  void shouldNotDeleteNonexistingUser() {
    service.createUser(user);
    restTemplate.delete(
        URI.create("http://localhost:" + serverPort + "/user/" + UUID.randomUUID()));
    User restUser = service.getById(user.getId());
    assertThat(restUser).usingRecursiveComparison().isEqualTo(user);
  }
}
