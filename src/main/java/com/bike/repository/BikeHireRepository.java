package com.bike.repository;

import com.bike.model.BikeHire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BikeHireRepository extends JpaRepository<BikeHire, Long> {

}
