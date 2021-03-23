package com.bike.service;

import com.bike.model.Bike;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public List<Bike> getList() {
        return bikeRepository.findAll();
    }

    public Bike getById(UUID id) {
        return bikeRepository.getOne(id);
    }

    public Bike addNewBike(Bike bike) {
        return bikeRepository.save(bike);
    }

    /**
     * Deletes a bike by id
     * @param bikeId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean deleteBike(UUID bikeId) {
        Bike bike = bikeRepository.getOne(bikeId);
        if (bike ==null){
            return false;
        }
        bikeRepository.deleteById(bikeId);
        return true;
    }
}
