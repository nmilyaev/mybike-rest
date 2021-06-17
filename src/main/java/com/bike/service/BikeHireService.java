package com.bike.service;

import com.bike.model.BikeHire;
import com.bike.repository.BikeHireRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

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

    // TODO - fill in the logic. Check if there is already a hire for this bike for the
    // given dates
    private boolean checkIfHireValid(BikeHire hire) {
        return true;
    }

    public void cancelHire(Long hireId) {
        repository.deleteById(hireId);
    }
}
