package com.bike.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity(name = "BikeHire")
@Table(name = "bike_hire")
public class BikeHire {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "hire_id", updatable = false, nullable = false)
    private Long id;

    /** Agreed deposit */
    @Column(name = "deposit")
    private BigDecimal deposit;

    /** Agreed daily rate */
    @Column(name = "dailyRate")
    private BigDecimal dailyRate;

    @Column(name = "firstDay")
    private LocalDate firstDay;

    @Column(name = "lastDay")
    private LocalDate lastDay;

    @OneToOne
    @JoinColumn(name="bike_id", nullable=false, foreignKey=@ForeignKey(name="bike_hire_bike_id"))
    private Bike bike;

    @OneToOne
    @JoinColumn(name="borrower_id", nullable=false, foreignKey=@ForeignKey(name="bike_hire_borrower_id"))
    private MybikeUser borrower;

    public BikeHire(Long id, BigDecimal deposit, BigDecimal dailyRate, LocalDate firstDay, LocalDate lastDay, Bike bike, MybikeUser borrower) {
        this.id = id;
        this.deposit = deposit;
        this.dailyRate = dailyRate;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.bike = bike;
        this.borrower = borrower;
    }
}
