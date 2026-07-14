package com.bike.dto;

import com.bike.model.Bike;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static java.math.RoundingMode.CEILING;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class BikeDto {
    @Transient
    private final int SCALE = 2;
    @Transient
    private final RoundingMode ROUNDING_MODE = CEILING;

    private UUID id;

    private String make;

    private String model;

    private BigDecimal value;

    private BigDecimal deposit;

    private BigDecimal dailyRate;

    private UserDto owner;

    public static BikeDto fromEntity(Bike bike) {
        if (bike == null) {
            return null;
        }

        return BikeDto.builder()
                .id(bike.getId())
                .make(bike.getMake())
                .model(bike.getModel())
                .value(bike.getValue())
                .deposit(bike.getDeposit())
                .dailyRate(bike.getDailyRate())
                .owner(UserDto.fromEntity(bike.getOwner()))
                .build();
    }

    public Bike toEntity() {
        return Bike.builder()
                .id(this.id)
                .make(this.make)
                .model(this.model)
                .value(this.value)
                .deposit(this.deposit)
                .dailyRate(this.dailyRate)
                .owner(this.owner == null ? null : this.owner.toEntity())
                .build();
    }
}
