package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.Borrower;
import com.bike.model.Lender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class LenderServiceIntegrationTest {

    @Autowired
    LenderService service;

    @Test
    void shouldSaveLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Lender saved = service.addNewLender(lender);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(lender);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldSaveAndLoadLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        Lender saved = service.addNewLender(lender);
        Lender loaded = service.getById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(lender);
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldDeleteLender() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        service.addNewLender(lender);
        boolean success = service.deleteLender(lender.getId());
        assertTrue(success);
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getById(lender.getId()));
        assertEquals("Unable to find com.bike.model.Lender with id " + lender.getId(), exception.getMessage());
    }

    @Test
    @Transactional
        // else service.getById fails to lazy-load the bike
    void shouldNotDeleteLenderByWrongId() {
        Lender lender = Lender.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com");
        service.addNewLender(lender);
        boolean success = service.deleteLender(UUID.randomUUID());
        assertFalse(success);
        Lender loaded = service.getById(lender.getId());
        assertNotNull(loaded.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(lender);
    }
}
