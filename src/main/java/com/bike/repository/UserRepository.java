package com.bike.repository;

import com.bike.model.MybikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<MybikeUser, UUID> {

    MybikeUser findByEmail(String email);
}
