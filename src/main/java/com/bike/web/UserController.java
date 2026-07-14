package com.bike.web;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.bike.dto.UserDto;
import com.bike.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping
  @ResponseStatus(OK)
  public List<UserDto> getUserList() {
    return userService.getAll().stream().map(UserDto::fromEntity).toList();
  }

  @GetMapping(value = "/{userId}")
  @ResponseStatus(OK)
  public UserDto getUser(@PathVariable UUID userId) {
    return UserDto.fromEntity(userService.getById(userId));
  }

  @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
  @ResponseStatus(CREATED)
  public UserDto createUser(@RequestBody UserDto user) {
    var newUser = userService.createUser(user.toEntity());
    return UserDto.fromEntity(newUser);
  }

  @DeleteMapping(value = "/{userId}")
  public void deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
  }
}
