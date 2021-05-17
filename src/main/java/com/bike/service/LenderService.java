package com.bike.service;

import com.bike.model.Lender;
import com.bike.repository.LenderRepository;
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
public class LenderService {

    private final LenderRepository lenderRepository;

    @Autowired
    public LenderService(LenderRepository lenderRepository) {
        this.lenderRepository = lenderRepository;
    }

    public List<Lender> getList() {
        return lenderRepository.findAll();
    }

    public Lender getById(UUID id) throws EntityNotFoundException {
        Lender lender;
        try {
            lender = lenderRepository.getOne(id);
        } catch (JpaObjectRetrievalFailureException ex) {
            throw new EntityNotFoundException(ex.getCause().getMessage());
        }
        return lender;
    }

    public Lender addNewLender(Lender lender) {
        return lenderRepository.save(lender);
    }

    /**
     * Deletes a lender by id
     *
     * @param lenderId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean deleteLender(UUID lenderId) {
        try {
            lenderRepository.deleteById(lenderId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }
}
