package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;

    private final BikeRepository bikeRepository;

    @Autowired
    public UserService(UserRepository userRepository, BikeRepository bikeRepository) {
        this.userRepository = userRepository;
        this.bikeRepository = bikeRepository;
    }

    public List<MybikeUser> getAll() {
        return userRepository.findAll();
    }

    public MybikeUser getById(UUID id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Unable to find com.bike.model.MybikeUser with id " + id));
    }

    public MybikeUser createUser(MybikeUser user) {
        MybikeUser userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail != null) {
            throw new IllegalArgumentException("User with this email already exists: " + userWithSameEmail.getId());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes a user by id
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

    public List<Bike> getUserBikes(MybikeUser user){
        return bikeRepository.findAllByOwner(user);
    }
}
