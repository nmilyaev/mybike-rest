package com.bike.web;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(classes = BorrowMyBikeApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractControllerIntegrationTest {

    @Value("${server.port}")
    int serverPort;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BikeRepository bikeRepository;

    MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @AfterEach
    void cleanUp() {
        bikeRepository.deleteAll();
        userRepository.deleteAll();
    }
}
