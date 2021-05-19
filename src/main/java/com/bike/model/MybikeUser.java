package com.bike.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Entity(name = "mybike_user")
@Table(name = "mybike_user")
public class MybikeUser {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;
    @Column(name = "surname", nullable = false, length = 50)
    private String surname;
    @Column(name = "address")
    private String address;
    @Column(name = "postcode", nullable = false, length = 10)
    private String postcode;
    @Column(name = "city", length = 50)
    private String city;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "bike_offer",
            joinColumns = @JoinColumn(name = "user_id", foreignKey=@ForeignKey(name="bike_offer_user_id")),
            inverseJoinColumns = @JoinColumn(name = "bike_id", foreignKey=@ForeignKey(name="bike_offer_bike_id"))
    )
    private Set<Bike> bikeOffers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "bike_hire",
            joinColumns = @JoinColumn(name = "user_id", foreignKey=@ForeignKey(name="bike_hire_user_id")),
            inverseJoinColumns = @JoinColumn(name = "bike_id", foreignKey=@ForeignKey(name="bike_hire_bike_id"))
    )
    private Set<Bike> bikeHires = new HashSet<>();

    public MybikeUser(String firstname, String surname, String email, String postcode, String password) {
        this.firstname = firstname;
        this.surname = surname;
        this.postcode = postcode;
        this.email = email;
        this.password = password;
    }

    public static MybikeUser createWithRequiredFields(String firstname, String surname, String email, String postcode, String password) {
        return new MybikeUser(firstname, surname, email, postcode, password);
    }
}


