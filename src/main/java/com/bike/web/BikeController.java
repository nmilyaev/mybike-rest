package com.bike.web;

import com.bike.dto.BikeDto;
import com.bike.dto.MybikeUserDto;
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
    public List<BikeDto> getBikesList() {
        return bikeService.getList().stream().map(BikeDto::fromEntity).toList();
    }

    @GetMapping(value = "/{bikeId}")
    public BikeDto getBikes(@PathVariable UUID bikeId) {
        return BikeDto.fromEntity(bikeService.getById(bikeId));
    }

    @PostMapping(value = "/createBike", consumes = "application/json", produces = "application/json")
    public void createBike(@RequestBody BikeDto bikeDto) {
        bikeService.addNewBike(bikeDto.toEntity());
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


