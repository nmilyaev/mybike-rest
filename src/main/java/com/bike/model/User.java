package com.bike.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Entity(name = "MyBikeUser")
@Table(name = "mybike_user", schema = "mybike")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
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

  public User(String firstname, String surname, String email, String postcode, String password) {
    this.firstname = firstname;
    this.surname = surname;
    this.postcode = postcode;
    this.email = email;
    this.password = password;
  }
}
