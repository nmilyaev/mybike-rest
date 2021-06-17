package com.bike.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Entity(name = "MyBikeUser")
@Table(name = "mybike_user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonInclude(NON_NULL)
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

    /**
     * Just an indication for JSON that the top object's name is "MyBikeUser"
     */
    public static class Root {
        @JsonProperty("MybikeUser")
        public MybikeUser mybikeUser;
    }
}


