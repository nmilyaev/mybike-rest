package com.bike.dto;

import com.bike.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"email"})
@JsonRootName("MybikeUser")
@JsonInclude(NON_NULL)
public class UserDto {
    protected UUID id;
    protected String firstname;
    protected String surname;
    protected String address;
    protected String postcode;
    protected String city;
    protected String email;
    protected String phone;
    @JsonProperty(access = WRITE_ONLY)
    protected String password;

    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .address(user.getAddress())
                .postcode(user.getPostcode())
                .city(user.getCity())
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(user.getPassword())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .firstname(this.firstname)
                .surname(this.surname)
                .address(this.address)
                .postcode(this.postcode)
                .city(this.city)
                .email(this.email)
                .phone(this.phone)
                .password(this.password)
                .build();
    }
}


