package com.bike.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "BikeOffer")
@Table(name = "bike_offer")
public class BikeOffer extends BikeListing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "offer_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "dailyRate")
    private BigDecimal dailyRate;

    @OneToOne
    @JoinColumn(name="bike_id", nullable=false, foreignKey=@ForeignKey(name="bike_listing_bike_id"))
    private Bike bike;
}
