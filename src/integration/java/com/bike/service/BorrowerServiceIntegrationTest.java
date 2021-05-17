package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Borrower;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BorrowerServiceIntegrationTest {

    @Autowired
    BorrowerService service;

    @Test
    void shouldSaveBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Borrower saved = service.addNewBorrower(borrower);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(borrower);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldSaveAndLoadBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Borrower saved = service.addNewBorrower(borrower);
        Borrower loaded = service.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(borrower);
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldDeleteBorrower() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        service.addNewBorrower(borrower);
        boolean success = service.deleteBorrower(borrower.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(borrower.getId()));
        assertEquals("Unable to find com.bike.model.Borrower with id " + borrower.getId(), exception.getMessage());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldNotDeleteBorrowerByWrongId() {
        Borrower borrower = Borrower.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        service.addNewBorrower(borrower);
        boolean success = service.deleteBorrower(UUID.randomUUID());
        assertFalse(success);
        Borrower loaded = service.getById(borrower.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(borrower);
    }
}
