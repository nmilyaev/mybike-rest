package com.bike.repository;

import com.bike.model.Borrower;
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
public class BorrowerRepositoryIntegrationTest {
    @Autowired
    BorrowerRepository repository;

    @Test
    void shouldSaveBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Borrower saved = repository.save(borrower);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(borrower);
    }

    @Test
    @Transactional
    void shouldSaveAndLoadBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Borrower saved = repository.save(borrower);
        Borrower loaded = repository.getOne(saved.getId());
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(borrower);
    }

    @Test
    @Transactional
    void shouldDeleteBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Borrower saved = repository.save(borrower);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getOne(saved.getId()));
        assertEquals("Unable to find com.bike.model.Borrower with id " + saved.getId(), exception.getCause().getMessage());
    }
}
