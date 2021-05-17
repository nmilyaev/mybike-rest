package com.bike.repository;

import com.bike.model.Lender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
public class LenderRepositoryIntegrationTest {
    @Autowired
    LenderRepository repository;

    @Test
    void shouldSaveLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Lender saved = repository.save(lender);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(lender);
    }

    @Test
    @Transactional
    void shouldSaveAndLoadLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Lender saved = repository.save(lender);
        Lender loaded = repository.getOne(saved.getId());
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(lender);
    }

    @Test
    @Transactional
    void shouldDeleteLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Lender saved = repository.save(lender);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getOne(saved.getId()));
        assertEquals("Unable to find com.bike.model.Lender with id " + saved.getId(), exception.getCause().getMessage());
    }
}
