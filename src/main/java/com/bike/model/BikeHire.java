package com.bike.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity(name = "BikeHire")
@Table(name = "bike_hire")
public class BikeHire {
    @Transient
    private final int SCALE = 2;
    @Transient
    private final RoundingMode ROUNDING_MODE = RoundingMode.CEILING;

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
    private LocalDate startDate;

    @Column(name = "lastDay")
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name="bike_id", nullable=false, foreignKey=@ForeignKey(name="bike_hire_bike_id"))
    private Bike bike;

    @OneToOne
    @JoinColumn(name="borrower_id", nullable=false, foreignKey=@ForeignKey(name="bike_hire_borrower_id"))
    private MybikeUser borrower;

    public BigDecimal getDeposit() {
        return deposit.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getDailyRate() {
        return dailyRate.setScale(SCALE, ROUNDING_MODE);
    }

    public BikeHire(Long id, BigDecimal deposit, BigDecimal dailyRate, LocalDate startDate, LocalDate endDate, Bike bike, MybikeUser borrower) {
        this.id = id;
        this.deposit = deposit;
        this.dailyRate = dailyRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bike = bike;
        this.borrower = borrower;
    }

    @Override
    public String toString() {
        return "BikeHire{" +
                "id=" + id +
                ", deposit=" + deposit +
                ", dailyRate=" + dailyRate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", bike=" + bike +
                ", borrower=" + borrower +
                '}';
    }
}
