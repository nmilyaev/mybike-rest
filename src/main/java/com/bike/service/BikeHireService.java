package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.repository.BikeHireRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BikeHireService {

    private final BikeHireRepository repository;

    @Autowired
    public BikeHireService(BikeHireRepository repository) {
        this.repository = repository;
    }

    public List<BikeHire> getList() {
        return repository.findAll();
    }

    public BikeHire getById(Long id) throws EntityNotFoundException {
        BikeHire hire;
        try {
            hire = repository.getOne(id);
        } catch (JpaObjectRetrievalFailureException ex) {
            throw new EntityNotFoundException(ex.getCause().getMessage());
        }
        return hire;
    }

    public BikeHire createHire(BikeHire hire) {
        checkIfHireValid(hire);
        return repository.save(hire);
    }

    // TODO - fill in the logic. Check if there is already a hire for this bike for the
    // given dates
    private boolean checkIfHireValid(BikeHire hire) {
        return true;
    }

    /**
     * Cancels a bike hire by id
     *
     * @param hireId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean cancelHire(Long hireId) {
        try {
            repository.deleteById(hireId);
        }
        catch (EmptyResultDataAccessException ex){
            return false;
        }
        return true;
    }
}
