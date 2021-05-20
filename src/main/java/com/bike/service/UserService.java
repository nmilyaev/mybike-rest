package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BikeService bikeService;

    @Autowired
    public UserService(UserRepository userRepository, BikeService bikeService) {
        this.userRepository = userRepository;
        this.bikeService = bikeService;
    }

    public List<MybikeUser> getAll() {
        return userRepository.findAll();
    }

    public MybikeUser getById(UUID id) throws EntityNotFoundException {
        MybikeUser user;
        try {
            user = userRepository.getOne(id);
        } catch (JpaObjectRetrievalFailureException ex) {
            throw new EntityNotFoundException(ex.getCause().getMessage());
        }
        return user;
    }

    public MybikeUser addNewUser(MybikeUser user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a borrower by id
     *
     * @param userId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean deleteUser(UUID userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }

    public Bike listNewBike(MybikeUser user, Bike bike) {
        bike.setOwner(user);
        Set<Bike> bikeOffers = user.getBikeOffers();
        bikeService.addNewBike(bike);
        bikeOffers.add(bike);
        userRepository.save(user);
        return bike;
    }
}
