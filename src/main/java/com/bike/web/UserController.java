package com.bike.web;

import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<MybikeUser> getUserList() {
        return userService.getAll();
    }

    @GetMapping(value = "/{userId}")
    public MybikeUser getUser(@PathVariable UUID userId) {
        return userService.getById(userId);
    }

    @PostMapping(value = "/createUser", consumes = "application/json", produces = "application/json")
    public MybikeUser createUser(@RequestBody MybikeUser user) {
        return userService.createUser(user);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }
}


