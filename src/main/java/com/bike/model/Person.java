package com.bike.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"})
@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstname;
    @Column(name = "surname", nullable = false, length = 50)
    private String surname;
    @Column(name = "address")
    private String address;
    @Column(name = "postcode", length = 10)
    private String postcode;
    @Column(name = "city", length = 50)
    private String city;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "phone", length = 20)
    private String phone;

    public Person(String firstname, String surname, String email) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
    }
}


