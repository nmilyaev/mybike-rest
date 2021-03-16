package com.bike.service;

import com.bike.model.Bike;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BikeServiceTest {

    @InjectMocks
    BikeService service;

    @Test
    public void shouldGetAllBikes() {
        // given
        // when
        List<Bike> bikes = service.getAll();

        //then
        Bike bike1 = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        assertBikesTheSame(bike1, bikes.get(0));
        assertBikesTheSame(bike2, bikes.get(1));
    }

    private void assertBikesTheSame(Bike bike1, Bike bike2) {
        assertEquals(bike1.getMake(), bike2.getMake());
        assertEquals(bike1.getModel(), bike2.getModel());
        assertEquals(bike1.getWorth(), bike2.getWorth());

    }
}