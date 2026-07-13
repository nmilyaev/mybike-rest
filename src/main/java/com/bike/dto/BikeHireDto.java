package com.bike.dto;

import com.bike.model.BikeHire;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static java.math.RoundingMode.CEILING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
@Builder
public class BikeHireDto {
    @Transient
    private final int SCALE = 2;
    @Transient
    private final RoundingMode ROUNDING_MODE = CEILING;

    private Long id;

    /** Agreed deposit */
    private BigDecimal deposit;

    /** Agreed daily rate */
    private BigDecimal dailyRate;

    private LocalDate startDate;

    private LocalDate endDate;

    private BikeDto bikeDto;

    private MybikeUserDto borrower;

    public BigDecimal getDeposit() {
        return deposit.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getDailyRate() {
        return dailyRate.setScale(SCALE, ROUNDING_MODE);
    }

    public static BikeHireDto fromEntity(BikeHire bikeHire) {
        if (bikeHire == null) {
            return null;
        }

        return BikeHireDto.builder()
                .id(bikeHire.getId())
                .deposit(bikeHire.getDeposit())
                .dailyRate(bikeHire.getDailyRate())
                .startDate(bikeHire.getStartDate())
                .endDate(bikeHire.getEndDate())
                .bikeDto(BikeDto.fromEntity(bikeHire.getBike()))
                .borrower(MybikeUserDto.fromEntity(bikeHire.getBorrower()))
                .build();
    }
}
