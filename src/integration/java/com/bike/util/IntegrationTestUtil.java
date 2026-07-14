package com.bike.util;

import com.bike.model.User;

public class IntegrationTestUtil {

    public static User aMybikeUser() {
        return aMybikeUser("Nestor", "Miller", "n.m@mail.com", "SW9 1NR", "password");
    }

    public static User aMybikeUser(String firstname, String surname, String email, String postcode, String password) {
        return User.builder().firstname(firstname).surname(surname).email(email).postcode(postcode).password(password).build();
    }
}
