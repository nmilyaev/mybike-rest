package com.bike.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Entity(name = "bike")
@Table(name = "bike")
public class Bike {

    @Id
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
