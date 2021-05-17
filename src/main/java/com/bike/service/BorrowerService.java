package com.bike.service;

import com.bike.model.Borrower;
import com.bike.repository.BorrowerRepository;
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
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public List<Borrower> getList() {
        return borrowerRepository.findAll();
    }

    public Borrower getById(UUID id) throws EntityNotFoundException {
        Borrower borrower;
        try {
            borrower = borrowerRepository.getOne(id);
        } catch (JpaObjectRetrievalFailureException ex) {
            throw new EntityNotFoundException(ex.getCause().getMessage());
        }
        return borrower;
    }

    public Borrower addNewBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    /**
     * Deletes a borrower by id
     *
     * @param borrowerId - searched id
     * @return true or false depending on the success of the operation (i.e. whether the entry is found)
     */
    public boolean deleteBorrower(UUID borrowerId) {
        try {
            borrowerRepository.deleteById(borrowerId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }
}
