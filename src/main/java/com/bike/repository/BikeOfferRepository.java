package com.bike.repository;

import com.bike.model.BikeOffer;
import com.bike.model.MybikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BikeOfferRepository extends JpaRepository<BikeOffer, Long> {

    @Query("SELECT o FROM BikeOffer o, Bike b \n" +
            "WHERE o.bike=b.id AND b.owner=:user")
    List<BikeOffer> findAllByBikeOwner(@Param("user") MybikeUser user);
}
