package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeHireRepository;
import com.bike.repository.BikeRepository;
import com.bike.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BikeRepository bikeRepository;
    private final BikeHireRepository bikeHireRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BikeRepository bikeRepository,
                       BikeHireRepository bikeHireRepository) {
        this.userRepository = userRepository;
        this.bikeRepository = bikeRepository;
        this.bikeHireRepository = bikeHireRepository;
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
            throw new IllegalArgumentException("User with this email already exists: " + userWithSameEmail.getEmail());
        }
        return userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        try{
            userRepository.deleteById(userId);
        }
        catch(Exception ex){
            throw new EntityNotFoundException("Unable to delete user with id " + userId);
        }
    }

    public List<Bike> getUserBikes(MybikeUser user) {
        return bikeRepository.findAllByOwner(user);
    }

    public List<BikeHire> getUserHires(MybikeUser borrower) {
        return bikeHireRepository.findAllByBorrower(borrower);
    }
}
