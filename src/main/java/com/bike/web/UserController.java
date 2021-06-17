package com.bike.web;

import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/user")
@Transactional
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
        log.info(".............User: {}", user);
        return userService.createUser(user);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@PathVariable UUID userId) {
        // TODO - add proper exception handler
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(CREATED);
        }
        catch (Exception ex){
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}


