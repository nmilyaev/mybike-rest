package com.bike.dto;

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

    public static BikeHireDto fromEntity(com.bike.model.BikeHire bikeHire) {
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

    public com.bike.model.BikeHire toEntity() {
        return com.bike.model.BikeHire.builder()
                .id(this.id)
                .deposit(this.deposit)
                .dailyRate(this.dailyRate)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .bike(this.bikeDto == null ? null : this.bikeDto.toEntity())
                .borrower(this.borrower == null ? null : this.borrower.toEntity())
                .build();
    }
}
