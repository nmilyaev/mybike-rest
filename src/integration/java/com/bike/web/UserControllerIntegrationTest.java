package com.bike.web;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.MybikeUser;
import com.bike.service.UserService;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BorrowMyBikeApplication.class)
class UserControllerIntegrationTest {
    @Autowired
    private UserService service;

    @Autowired
    private UserController controller;

    MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    @Transactional
    void shouldCreateUser() {
        controller.createUser(user);
        assertNotNull(user.getId());
        MybikeUser found = service.getById(user.getId());
        assertThat(found)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
    void shouldNotCreateUserTwice() {
        controller.createUser(user);
        MybikeUser user1 = MybikeUser.createWithRequiredFields("Nick", "Mills", "n.m@mail.com", "SW9 2NR", "password");
        assertNotNull(user.getId());
        MybikeUser found = service.getById(user.getId());
        assertThat(found)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void getUserList() {
    }

    @Test
    void getUser() {
    }

    @Test
    void deleteUser() {
    }
}