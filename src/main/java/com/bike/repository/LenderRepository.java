package com.bike.repository;

import com.bike.model.Lender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LenderRepository extends JpaRepository<Lender, UUID> {

}
