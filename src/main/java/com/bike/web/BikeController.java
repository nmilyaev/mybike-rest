package com.bike.web;

import com.bike.dto.BikeDto;
import com.bike.service.BikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bike")
public class BikeController {
    private final BikeService bikeService;

    @GetMapping
    public ResponseEntity<List<BikeDto>> getBikesList() {
        return ResponseEntity.ok(bikeService.getList().stream().map(BikeDto::fromEntity).toList());
    }

    @GetMapping(value = "/{bikeId}")
    public ResponseEntity<BikeDto> getBikes(@PathVariable UUID bikeId) {
        return ResponseEntity.ok(BikeDto.fromEntity(bikeService.getById(bikeId)));
    }

    @PostMapping(value = "/createBike", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BikeDto> createBike(@RequestBody BikeDto bikeDto) {
        return ResponseEntity.ok(BikeDto.fromEntity(bikeService.addNewBike(bikeDto.toEntity())));
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


