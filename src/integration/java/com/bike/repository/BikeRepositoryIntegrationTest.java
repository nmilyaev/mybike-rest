package com.bike.repository;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Not a true test; that is just to test how @DataJpaTest works
 */

@Slf4j
@DataJpaTest
@ContextConfiguration(classes = BorrowMyBikeApplication.class)
@ExtendWith(MockitoExtension.class)
public class BikeRepositoryIntegrationTest {
    @Autowired
    BikeRepository repository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldSaveBike() {
        MybikeUser user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userRepository.save(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = repository.save(bike);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(bike);
        log.info("............UUID: {}", saved.getId());
    }

    @Test
    void shouldSaveAndLoadBike() {
        MybikeUser user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userRepository.save(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = repository.save(bike);
        Bike loaded = repository.getReferenceById(saved.getId());
        assertNotNull(saved.getId());
        assertThat(loaded)
                .usingRecursiveComparison()
                .isEqualTo(bike);
    }

    @Test
    void shouldDeleteBike() {
        MybikeUser user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userRepository.save(user);
        Bike bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        Bike saved = repository.save(bike);
        repository.delete(saved);
        Throwable exception = assertThrows(JpaObjectRetrievalFailureException.class, () -> repository.getReferenceById(saved.getId()));
        assertEquals("Unable to find com.bike.model.Bike with id " + saved.getId(), exception.getCause().getMessage());
    }
}
