package com.bike.web;

import com.bike.model.Bike;
import com.bike.model.MybikeUser;
import com.bike.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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
    public Bike getBikes(@PathVariable UUID bikeId) {
        return bikeService.getById(bikeId);
    }

    @PostMapping(value = "/createBike", consumes = "application/json", produces = "application/json")
    public Bike createBike(@RequestBody Bike bike) {
        return bikeService.addNewBike(bike);
    }

    @PostMapping
    public Bike getBikesList(@RequestBody Bike bike) {
        return bikeService.addNewBike(bike);
    }

    @DeleteMapping(value = "/{bikeId}")
    public ResponseEntity<Boolean> deleteBike(@PathVariable UUID bikeId) {
        // TODO - add proper exception handler
        try {
            bikeService.deleteBike(bikeId);
            return new ResponseEntity<>(NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}


