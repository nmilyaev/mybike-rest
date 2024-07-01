package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.repository.BikeHireRepository;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
public class BikeService {

    private final BikeRepository bikeRepository;
    private final BikeHireRepository bikeHireRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository,
                       BikeHireRepository bikeHireRepository) {
        this.bikeRepository = bikeRepository;
        this.bikeHireRepository = bikeHireRepository;
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

    public List<BikeHire> getHiresForBike(Bike bike) {
        return bikeHireRepository.findAllByBike(bike);
    }

    public List<BikeHire> getFutureHiresForBike(Bike bike) {
        LocalDate today = now();
        return bikeHireRepository.findAllByBikeAndStartDateNowOrLater(bike, today);
    }
}
