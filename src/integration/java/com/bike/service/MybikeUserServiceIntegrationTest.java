package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class MybikeUserServiceIntegrationTest {

    @Autowired
    private UserService service;
    private MybikeUser user;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    void shouldSaveUser() {
        MybikeUser saved = service.addNewUser(user);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldSaveAndLoadUser() {
        MybikeUser saved = service.addNewUser(user);
        MybikeUser loaded = service.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldDeleteUser() {
        service.addNewUser(user);
        boolean success = service.deleteUser(user.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(user.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + user.getId(), exception.getMessage());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldNotDeleteUserByWrongId() {
        service.addNewUser(user);
        boolean success = service.deleteUser(UUID.randomUUID());
        assertFalse(success);
        MybikeUser loaded = service.getById(user.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void shouldSaveAndFetchAllUsers() {
        MybikeUser saved1 = service.addNewUser(user);
        assertNotNull(saved1.getId());
        setUp();
        assertNull(user.getId());
        MybikeUser saved2 = service.addNewUser(user);
        assertNotNull(saved2.getId());
        List<MybikeUser> allUsers = service.getAll();
        assertEquals(2, allUsers.size());
        assertThat(allUsers.get(0))
                .usingRecursiveComparison()
                .isEqualTo(saved1);
        assertThat(allUsers.get(1))
                .usingRecursiveComparison()
                .isEqualTo(saved2);
    }
}
