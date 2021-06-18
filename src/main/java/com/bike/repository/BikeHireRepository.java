package com.bike.repository;

import com.bike.model.Bike;
import com.bike.model.BikeHire;
import com.bike.model.MybikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BikeHireRepository extends JpaRepository<BikeHire, Long> {

    List<BikeHire> findAllByBorrower(MybikeUser borrower);

    List<BikeHire> findAllByBike(Bike bike);

    @Query("select bh from BikeHire bh where bh.bike = :bike and bh.startDate >=:firstDay")
    List<BikeHire> findAllByBikeAndStartDateNowOrLater(@Param("bike")Bike bike, @Param("firstDay") LocalDate firstDay);
}
