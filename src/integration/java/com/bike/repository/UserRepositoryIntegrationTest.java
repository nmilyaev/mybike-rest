package com.bike.repository;

import com.bike.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import org.springframework.transaction.annotation.Transactional;

import static com.bike.util.IntegrationTestUtil.aMybikeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Not a true test; that is just to test how @DataJpaTest works
 */

@Slf4j
@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp(){
        user = aMybikeUser();
    }

    @Test
    void shouldSaveUser() {
        User saved = repository.save(user);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
    void shouldSaveAndLoadUser() {
        User saved = repository.save(user);
        User loaded = repository.getReferenceById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
    void shouldDeleteUser() {
        User saved = repository.save(user);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getReferenceById(saved.getId()));
        assertEquals("Unable to find com.bike.model.User with id " + saved.getId(), exception.getCause().getMessage());
    }
}
