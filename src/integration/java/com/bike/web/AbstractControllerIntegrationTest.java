package com.bike.web;

import com.bike.BorrowMyBikeApplication;
import com.bike.dto.MybikeUserDto;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(classes = BorrowMyBikeApplication.class,
        webEnvironment = DEFINED_PORT)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("integration")
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
    MybikeUserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = MybikeUserDto.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        user = userDto.toEntity();
        userDto.setPassword(user.getPassword());
    }

    @AfterEach
    void cleanUp() {
        bikeRepository.deleteAll();
        userRepository.deleteAll();
    }
}
