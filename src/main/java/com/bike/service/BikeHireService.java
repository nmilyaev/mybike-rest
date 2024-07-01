package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.repository.BikeHireRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@Slf4j
@Service
@Transactional
public class BikeHireService {

    private final BikeHireRepository repository;

    @Autowired
    public BikeHireService(BikeHireRepository repository) {
        this.repository = repository;
    }

    public BikeHire getById(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Unable to find com.bike.model.BikeHire with id " + id));
    }

    public List<BikeHire> getAllHires() {
        return repository.findAll();
    }

    public BikeHire saveHire(BikeHire hire) {
        checkIfHireValid(hire);
        return repository.save(hire);
    }

    //  1. Check the new hire is not in the past
    //  2. Check that start date is before end date
    //  3. Check if there is already a hire for this bike for the given dates
    private void checkIfHireValid(BikeHire hire) {
        LocalDate today = now();
        // 1. Check the new hire is not in the past
        if (hire.getStartDate().isBefore(today)) {
            throw new IllegalArgumentException("New hire start date cannot be in the past");
        }
        // 2. Check that start date is before end date; Minimum hire is one day when firstDay = lastDay
        if (hire.getEndDate().isBefore(hire.getStartDate())) {
            throw new IllegalArgumentException("New hire first day cannot be before the last day");
        }
        // 3. Check if there is already a hire for this bike for the given dates.
        // A new hire is only allowed to start next day after previous hire finished.
        Bike bike = hire.getBike();
        List<BikeHire> futureHiresForBike = repository.findAllByBikeAndStartDateNowOrLater(bike, today);
        List<BikeHire> foundConflictingHires = futureHiresForBike.stream().filter(bh ->
                !hire.getStartDate().isAfter(bh.getEndDate()) && !hire.getEndDate().isBefore(bh.getStartDate())
        ).collect(Collectors.toList());
        if (foundConflictingHires.size() > 0) {
            throw new IllegalArgumentException("The bike is already booked for the dates specified");
        }
    }

    public void cancelHire(Long hireId) {
        repository.deleteById(hireId);
    }
}
