package com.bike.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity(name = "Bike")
@Table(name = "bike", schema = "mybike")
public class Bike {
    @Transient
    private final int SCALE = 2;
    @Transient
    private final RoundingMode ROUNDING_MODE = RoundingMode.CEILING;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "bike_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "make", length = 100)
    private String make;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "worth", precision = 8, scale = 2)
    private BigDecimal value;

    @Column(name = "deposit", precision = 8, scale = 2)
    private BigDecimal deposit;

    @Column(name = "daily_rate", precision = 8, scale = 2)
    private BigDecimal dailyRate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "bike_owner_id"))
    private User owner;

    public Bike(UUID id, String make, String model, BigDecimal value, User user) {
        this(make, model, value, user);
        this.id = id;
    }

    public Bike(String make, String model, BigDecimal value, User user) {
        this.make = make;
        this.model = model;
        this.value = value;
        this.owner = user;
        this.deposit = BigDecimal.ZERO;
        this.dailyRate = BigDecimal.ZERO;
    }

    public BigDecimal getValue() {
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getDeposit() {
        return deposit.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getDailyRate() {
        return dailyRate.setScale(SCALE, ROUNDING_MODE);
    }

    @Override
    public String toString() {
        return "Bike{id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", value=" + value +
                ", deposit=" + deposit +
                ", dailyRate=" + dailyRate +
                ", owner=" + owner +
                '}';
    }
}
