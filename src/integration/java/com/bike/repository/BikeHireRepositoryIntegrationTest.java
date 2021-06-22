package com.bike.repository;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BikeHireRepositoryIntegrationTest {
    @Autowired
    BikeHireRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BikeRepository bikeRepository;

    @Test
    void shouldFindByBikeAndStartDate() {
        MybikeUser user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userRepository.save(user);
        Bike bike1 = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeRepository.save(bike1);
        Bike bike2 = new Bike("Dawes", "Galaxy", BigDecimal.valueOf(180.00), user);
        bikeRepository.save(bike2);
        LocalDate now = now();
        BikeHire hire1 = BikeHire.builder()
                .bike(bike1)
                .borrower(user)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        repository.save(hire1);
        BikeHire hire2 = BikeHire.builder()
                .bike(bike2)
                .borrower(user)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        repository.save(hire2);
        BikeHire hire3 = BikeHire.builder()
                .bike(bike2)
                .borrower(user)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(2))
                .build();
        repository.save(hire3);
        List<BikeHire> allByBikeAndStartDateNowOrLater = repository.findAllByBikeAndStartDateNowOrLater(bike2, now);
        assertEquals(1, allByBikeAndStartDateNowOrLater.size());
        assertThat(allByBikeAndStartDateNowOrLater.get(0))
                .usingRecursiveComparison()
                .isEqualTo(hire2);
    }
}
