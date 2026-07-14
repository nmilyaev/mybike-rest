package com.bike.dto;

import com.bike.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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
    // TODO - do not expose password in JSON : @JsonProperty(access = WRITE_ONLY)
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
        User user = new User();

        user.setId(this.id);
        user.setFirstname(this.firstname);
        user.setSurname(this.surname);
        user.setAddress(this.address);
        user.setPostcode(this.postcode);
        user.setCity(this.city);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        user.setPassword(this.password);
        return user;
    }
}


