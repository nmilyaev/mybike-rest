package com.bike.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@MappedSuperclass
public abstract class BikeListing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "dailyRate")
    private BigDecimal dailyRate;

    @OneToOne
    @JoinColumn(name="bike_id", nullable=false)
    private Bike bike;
}
