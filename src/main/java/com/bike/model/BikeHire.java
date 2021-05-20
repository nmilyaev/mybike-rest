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
    @JoinColumn(name="borrower_id", nullable=false, foreignKey=@ForeignKey(name="bike_hire_borrower_id"))
    private MybikeUser borrower;
}
