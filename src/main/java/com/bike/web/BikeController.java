package com.bike.web;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

import com.bike.dto.BikeDto;
import com.bike.service.BikeService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bike")
public class BikeController {
  private final BikeService bikeService;

  @GetMapping(value = "/{bikeId}")
  @ResponseStatus(OK)
  public BikeDto getBike(@PathVariable UUID bikeId) {
    return BikeDto.fromEntity(bikeService.getById(bikeId));
  }

  @GetMapping
  @ResponseStatus(OK)
  public List<BikeDto> getBikesList() {
    return bikeService.getList().stream().map(BikeDto::fromEntity).toList();
  }

  @PostMapping(value = "/createBike", consumes = "application/json", produces = "application/json")
  @ResponseStatus(CREATED)
  public BikeDto createBike(@RequestBody BikeDto bikeDto) {
    var bike = bikeDto.toEntity();
    var savedBike = bikeService.addNewBike(bike);
    return BikeDto.fromEntity(savedBike);
  }

  @DeleteMapping(value = "/{bikeId}")
  @ResponseStatus(NO_CONTENT)
  public void deleteBike(@PathVariable UUID bikeId) {
    bikeService.deleteBike(bikeId);
  }
}
