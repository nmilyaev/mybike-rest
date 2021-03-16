package com.bike.service;

import com.bike.model.Bike;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BikeService {

    List<Bike> repository;

    public List<Bike> getAll(){
        Bike bike1 = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        repository = Arrays.asList(bike1, bike2);
        return repository;
    }

}
