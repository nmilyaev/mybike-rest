package com.bike.service;

import com.bike.model.Bike;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class BikeService {

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public List<Bike> getList() {
        return bikeRepository.findAll();
    }

    public Bike getById(UUID id) throws EntityNotFoundException {
        return bikeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Unable to find com.bike.model.Bike with id " + id));
    }

    public Bike addNewBike(Bike bike) {
        return bikeRepository.save(bike);
    }

    public void deleteBike(UUID bikeId) {
        bikeRepository.deleteById(bikeId);
    }
}
