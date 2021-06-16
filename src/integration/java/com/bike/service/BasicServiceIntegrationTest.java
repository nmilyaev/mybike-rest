package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.repository.BikeHireRepository;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BasicServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private BikeHireRepository bikeHireRpository;

    @AfterEach
    void cleanUp() {
        bikeHireRpository.deleteAll();
        bikeRepository.deleteAll();
        userRepository.deleteAll();
    }
}
