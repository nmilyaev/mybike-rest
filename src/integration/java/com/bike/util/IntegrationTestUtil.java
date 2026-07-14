package com.bike.util;

import com.bike.dto.UserDto;
import com.bike.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class IntegrationTestUtil {

    public static User aMybikeUser() {
        return aMybikeUser("Nestor", "Miller", "n.m@mail.com", "10 Kyiv avenue", "SW9 1NR", "London", "07777777777", "password");
    }

    public static User aMybikeUser(String firstname, String surname, String email, String address, String postcode, String city, String phone, String password) {
        return User.builder().firstname(firstname).surname(surname).email(email).address(address).postcode(postcode).city(city).phone(phone).password(password).build();
    }

    public static HttpEntity<String> createUserRequest(UserDto userDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        String requestJson = """
                {
                  "firstname": "%s",
                  "surname": "%s",
                  "address": "%s",
                  "postcode": "%s",
                  "city": "%s",
                  "email": "%s",
                  "phone": "%s",
                  "password": "%s"
                }
                """.formatted(
                userDto.getFirstname(),
                userDto.getSurname(),
                userDto.getAddress(),
                userDto.getPostcode(),
                userDto.getCity(),
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getPassword()
        );

        return new HttpEntity<>(requestJson, headers);
    }
}
