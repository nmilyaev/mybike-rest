package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class BikeService {

    private final BikeRepository bikeRepository;

    private final UserService userService;

    @Autowired
    public BikeService(BikeRepository bikeRepository,
                       UserService userService) {
        this.bikeRepository = bikeRepository;
        this.userService = userService;
    }

    public List<Bike> getList() {
        return bikeRepository.findAll();
    }

    public Bike getById(UUID id) throws EntityNotFoundException {
        return bikeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Unable to find com.bike.model.Bike with id " + id));
    }

    public Bike addNewBike(Bike bike) {
        //TODO: add bike to the user
        return bikeRepository.save(bike);
    }

    /**
     * Deletes a bike by id
     *
     * @param bikeId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean deleteBike(UUID bikeId) {
        try {
            // TODO - make sure this works
            Bike bike = getById(bikeId);
            MybikeUser owner = bike.getOwner();
            Set<Bike> bikeOffers = owner.getBikeOffers();
            bikeOffers.remove(bike);
            bikeRepository.deleteById(bikeId);
        } catch (EmptyResultDataAccessException | EntityNotFoundException ex) {
            return false;
        }
        return true;
    }
}
