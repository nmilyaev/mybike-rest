package com.bike.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity(name = "bike")
@Table(name = "bike")
public class Bike {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;
    @Column(name = "make")
    String make;
    @Column(name = "model")
    String model;
    @Column(name = "worth")
    BigDecimal worth;

    public Bike(UUID id, String make, String model, BigDecimal worth) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.worth = worth;
    }

    public Bike(String make, String model, BigDecimal worth) {
        this.make = make;
        this.model = model;
        this.worth = worth;
    }
}
