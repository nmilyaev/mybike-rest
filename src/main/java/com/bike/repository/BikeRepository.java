package com.bike.repository;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BikeRepository extends JpaRepository<Bike, UUID> {

    List<Bike> findAllByOwner(MybikeUser userId);
}
