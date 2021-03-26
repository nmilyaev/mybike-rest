package com.bike;

import com.bike.model.Bike;
import com.bike.repository.BikeRepository;
import com.bike.service.BikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BikeServiceTest {

    @Mock
    BikeRepository bikeRepository;

    @InjectMocks
    BikeService service;

    private List<Bike> bikes;

    @BeforeEach
    private void setUpRepository() {
        bikes = new ArrayList<>();
        Bike bike1 = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        bikes.add(bike1);
        bikes.add(bike2);
    }

    @Test
    void shouldGetAllBikes() {
        // given
        given(bikeRepository.findAll()).willReturn(bikes);

        // when
        List<Bike> bikes = service.getList();

        //then
        Bike bike1 = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        assertBikesTheSame(bike1, bikes.get(0));
        assertBikesTheSame(bike2, bikes.get(1));
    }

    @Test
    void shouldGetBikeById() {
        // given
        UUID bikeId = bikes.get(0).getId();
        given(bikeRepository.getOne(bikeId)).willReturn(bikes.get(0));

        // when
        Bike bike = service.getById(bikeId);

        //then
        Bike bike1 = new Bike(bikeId, "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        assertBikesTheSame(bike1, bike);
    }

    @Test
    void shouldCreateNewBike() {
        // given
        UUID bikeId = UUID.randomUUID();
        Bike bike3 = new Bike(bikeId, "Claud Butler", "Echelon", BigDecimal.valueOf(180.00));
        given(bikeRepository.save(bike3)).will((Answer) invocation -> {
            Object[] args = invocation.getArguments();
            Bike bike = (Bike) args[0];
            bikes.add(bike);
            return bike;
        });

        // when
        Bike bike = service.addNewBike(bike3);

        //then
        assertEquals(3, bikes.size());
        assertBikesTheSame(bike3, bikes.get(2));
        assertBikesTheSame(bike, bikes.get(2));
    }

    private void assertBikesTheSame(Bike bike1, Bike bike2) {
        assertEquals(bike1.getMake(), bike2.getMake());
        assertEquals(bike1.getModel(), bike2.getModel());
        assertEquals(bike1.getWorth(), bike2.getWorth());
    }
}