package com.bike.web;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.MybikeUser;
import com.bike.repository.UserRepository;
import com.bike.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(classes = BorrowMyBikeApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    MybikeUser user;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    public void givenFixedPortAsServerPort_whenReadServerProps_thenGetThePort() {
        int port = serverProperties.getPort();
        assertEquals(8081, port);
    }

    @Test
    void shouldCreateUser() {
        ResponseEntity<MybikeUser> response = this.restTemplate.postForEntity("http://localhost:" + serverPort + "/user/createUser", user,
                MybikeUser.class);
        assertEquals(OK, response.getStatusCode());
        MybikeUser restUser = response.getBody();
        user.setId(restUser.getId());
        assertNotNull(restUser.getId());
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void getUserList() {
    }

    @Test
    void getUser() {
        MybikeUser saved = service.createUser(user);
        MybikeUser restUser = this.restTemplate.getForObject("http://localhost:" + serverPort + "/user/" + saved.getId(),
                MybikeUser.class);
        assertThat(restUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void deleteUser() {
    }
}