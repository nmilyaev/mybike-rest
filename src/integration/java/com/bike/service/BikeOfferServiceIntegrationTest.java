package com.bike.service;

import com.bike.BorrowMyBikeApplication;
import com.bike.model.Bike;
import com.bike.model.BikeOffer;
import com.bike.model.MybikeUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(classes = BorrowMyBikeApplication.class)
public class BikeOfferServiceIntegrationTest {

    @Autowired
    private BikeOfferService service;
    @Autowired
    private UserService userService;
    @Autowired
    BikeService bikeService;

    private MybikeUser user;
    private Bike bike;

    @BeforeEach
    void setUp() {
        user = MybikeUser.createWithRequiredFields("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
        userService.addNewUser(user);
        bike = new Bike("Raleigh", "Pioneer", BigDecimal.valueOf(80.00), user);
        bikeService.addNewBike(bike);
    }

    @Test
    @Transactional
    void shouldCreateOffer() {
        BikeOffer offer = new BikeOffer(bike, BigDecimal.valueOf(80.00), BigDecimal.valueOf(10.00));
        assertNotNull(bike.getOwner());
        BikeOffer saved = service.createNewOffer(offer);
        assertNotNull(saved.getId());
        assertThat(saved)
                .usingRecursiveComparison()
                .isEqualTo(offer);
    }
}
