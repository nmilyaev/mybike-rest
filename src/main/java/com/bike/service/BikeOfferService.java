package com.bike.service;

import com.bike.model.Bike;
import com.bike.model.BikeOffer;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeOfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BikeOfferService {

    private final BikeOfferRepository offerRepository;

    @Autowired
    public BikeOfferService(BikeOfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<BikeOffer> getAllForUser(MybikeUser user) {
        return offerRepository.findAllByBikeOwner(user);
    }

    public Bike createNewOffer(BikeOffer offer) {
        return null;
    }
}
