package com.bike.service;

import com.bike.model.BikeOffer;
import com.bike.model.MybikeUser;
import com.bike.repository.BikeOfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class BikeOfferService {

    private final BikeOfferRepository offerRepository;
    private final UserService userService;

    @Autowired
    public BikeOfferService(BikeOfferRepository offerRepository,
                            UserService userService) {
        this.offerRepository = offerRepository;
        this.userService = userService;
    }

    public List<BikeOffer> getAllForUser(MybikeUser user) {
        return offerRepository.findAllByBikeOwner(user);
    }

    @Transactional
    public BikeOffer createNewOffer(BikeOffer offer) {
        MybikeUser bikeOwner = userService.getById(offer.getBike().getOwner().getId());
        bikeOwner.getBikeOffers().add(offer);
        return offerRepository.save(offer);
    }
}
