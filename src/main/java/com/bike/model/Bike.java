package com.bike.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Bike {

    UUID id;
    String make;
    String model;
    BigDecimal worth;

    public Bike(UUID id, String make, String model, BigDecimal worth) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.worth = worth;
    }
}
