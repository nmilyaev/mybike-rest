package com.bike.web;

import com.bike.model.Bike;
import com.bike.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bike")
public class BikeController {
    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public List<Bike> getBikesList() {
        return bikeService.getList();
    }

    @GetMapping(value = "/{bikeId}")
    public Bike getBikesList(@PathVariable UUID bikeId) {
        return bikeService.getById(bikeId);
    }

    @PostMapping
    public Bike getBikesList(@RequestBody Bike bike) {
        return bikeService.addNewBike(bike);
    }
}


