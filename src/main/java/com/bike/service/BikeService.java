package com.bike.service;

import com.bike.model.Bike;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BikeService {

    private final BikeRepository bikeRepository;
    private List<Bike> repository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public List<Bike> getList(){
        Bike bike1 = new Bike(UUID.randomUUID(), "Raleigh", "Pioneer", BigDecimal.valueOf(80.00));
        Bike bike2 = new Bike(UUID.randomUUID(), "Dawes", "Galaxy", BigDecimal.valueOf(100.00));
        repository = Arrays.asList(bike1, bike2);
        log.info("Bikes:{}", repository.stream().map(Objects::toString).collect(Collectors.joining("\n")));
        return repository;
    }

    public Bike getById(UUID id) {
        return repository.stream().filter(b->b.getId().equals(id)).findFirst().orElse(null);
    }
}
