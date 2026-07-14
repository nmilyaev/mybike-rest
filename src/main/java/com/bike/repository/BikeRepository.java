package com.bike.repository;

import com.bike.model.Bike;
import com.bike.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends JpaRepository<Bike, UUID> {

  List<Bike> findAllByOwner(User userId);
}
