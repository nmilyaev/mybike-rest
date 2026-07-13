package com.bike.web;

import com.bike.dto.MybikeUserDto;
import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<MybikeUserDto>> getUserList() {
        return ResponseEntity.ok(userService.getAll().stream().map(MybikeUserDto::fromEntity).toList());
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<MybikeUser> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<MybikeUserDto> createUser(@RequestBody MybikeUserDto user) {
        var newUser = userService.createUser(user.toEntity());
        return ResponseEntity.ok(MybikeUserDto.fromEntity(newUser));
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}


