package com.bike.repository;

import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Not a true test; that is just to test how @DataJpaTest works
 */

@Slf4j
@DataJpaTest
public class MybikeUserRepositoryIntegrationTest {
    @Autowired
    private UserRepository repository;
    private MybikeUser user;

    @BeforeEach
    void setUp(){
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    @Test
    void shouldSaveUser() {
        MybikeUser saved = repository.save(user);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
    void shouldSaveAndLoadUser() {
        MybikeUser saved = repository.save(user);
        MybikeUser loaded = repository.getOne(saved.getId());
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @Transactional
    void shouldDeleteUser() {
        MybikeUser saved = repository.save(user);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getOne(saved.getId()));
        assertEquals("Unable to find com.bike.model.MybikeUser with id " + saved.getId(), exception.getCause().getMessage());
    }
}
