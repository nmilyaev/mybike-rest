package com.bike.dto;

import com.bike.model.MybikeUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"email"})
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonInclude(NON_NULL)
public class MybikeUserDto {
    private UUID id;
    private String firstname;
    private String surname;
    private String address;
    private String postcode;
    private String city;
    private String email;
    private String phone;
    private String password;

    public static MybikeUserDto fromEntity(MybikeUser user) {
        if (user == null) {
            return null;
        }

        return MybikeUserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .surname(user.getSurname())
                .address(user.getAddress())
                .postcode(user.getPostcode())
                .city(user.getCity())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public MybikeUser toEntity() {
        MybikeUser user = new MybikeUser();

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


