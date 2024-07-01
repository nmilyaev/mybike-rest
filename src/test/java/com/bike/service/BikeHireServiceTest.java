package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeHireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BikeHireServiceTest {

    @Mock
    BikeHireRepository bikeRepository;

    @InjectMocks
    BikeHireService service;

    private List<Bike> bikes;

    private List<BikeHire> bikeHires;

    private MybikeUser owner;
    private MybikeUser borrower;

    @BeforeEach
    public void setUpRepository() {
        bikes = new ArrayList<>();
        bikeHires = new ArrayList<>();
        owner = new MybikeUser();
        borrower = new MybikeUser();
        Bike bike = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00), owner);
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00), owner);
        bikes.add(bike);
    }

    @Test
    void shouldNotAllowCreateHireInThePast() {
        // given
        LocalDate now = now();
        BikeHire hire = BikeHire.builder()
                .bike(bikes.get(0))
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(2))
                .build();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> service.saveHire(hire));
        assertEquals("New hire start date cannot be in the past", exception.getMessage());
    }

    @Test
    void shouldNotAllowEndDateBeforeStart() {
        // given
        LocalDate now = now();
        BikeHire hire = BikeHire.builder()
                .bike(bikes.get(0))
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.minusDays(1))
                .build();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> service.saveHire(hire));
        assertEquals("New hire first day cannot be before the last day", exception.getMessage());
    }

    @Test
    void shouldAllowNewHireEndBeforeExistingStarts() {
        // given
        LocalDate now = now();
        Bike bike = bikes.get(0);
        BikeHire existingHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(2))
                .endDate(now.plusDays(3))
                .build();
        BikeHire newHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(1))
                .build();
        bikeHires.add(existingHire);
        given(bikeRepository.findAllByBikeAndStartDateNowOrLater(bike, now)).willReturn(bikeHires);
        // when
        service.saveHire(newHire);
        // then
        // All works, no exception
    }

    @Test
    void shouldNotAllowNewHireStartSameDayAsExistingEnds() {
        // given
        LocalDate now = now();
        Bike bike = bikes.get(0);
        BikeHire existingHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(2))
                .endDate(now.plusDays(3))
                .build();
        BikeHire newHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        bikeHires.add(existingHire);
        given(bikeRepository.findAllByBikeAndStartDateNowOrLater(bike, now)).willReturn(bikeHires);
        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> service.saveHire(newHire));
        // then
        assertEquals("The bike is already booked for the dates specified", exception.getMessage());
    }

    @Test
    void shouldAllowExistingHireEndBeforeNewStarts() {
        // given
        LocalDate now = now();
        Bike bike = bikes.get(0);
        BikeHire existingHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        BikeHire newHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(3))
                .endDate(now.plusDays(4))
                .build();
        bikeHires.add(existingHire);
        given(bikeRepository.findAllByBikeAndStartDateNowOrLater(bike, now)).willReturn(bikeHires);
        // when
        service.saveHire(newHire);
        // then
        // All works, no exception
    }

    @Test
    void shouldNotAllowExistingHireStartSameDayAsNewEnds() {
        // given
        LocalDate now = now();
        Bike bike = bikes.get(0);
        BikeHire existingHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        BikeHire newHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(2))
                .endDate(now.plusDays(3))
                .build();
        bikeHires.add(existingHire);
        given(bikeRepository.findAllByBikeAndStartDateNowOrLater(bike, now)).willReturn(bikeHires);
        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> service.saveHire(newHire));
        // then
        assertEquals("The bike is already booked for the dates specified", exception.getMessage());
    }

    @Test
      void shouldCreateNewHireBetweenTwoExistingOnes() {
        // given
        LocalDate now = now();
        Bike bike = bikes.get(0);
        BikeHire existingHire1 = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now)
                .endDate(now.plusDays(2))
                .build();
        BikeHire existingHire2 = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(4))
                .endDate(now.plusDays(5))
                .build();
        BikeHire newHire = BikeHire.builder()
                .bike(bike)
                .borrower(borrower)
                .deposit(BigDecimal.valueOf(80.00))
                .dailyRate(BigDecimal.valueOf(10.00))
                .startDate(now.plusDays(3))
                .endDate(now.plusDays(3))
                .build();
        bikeHires.add(existingHire1);
        bikeHires.add(existingHire2);
        given(bikeRepository.findAllByBikeAndStartDateNowOrLater(bike, now)).willReturn(bikeHires);
        given(bikeRepository.save(newHire)).willReturn(newHire);
        // when
        BikeHire saved = service.saveHire(newHire);
        // then
    }

}