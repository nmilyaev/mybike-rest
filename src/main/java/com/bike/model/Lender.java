package com.bike.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "lender")
@Table(name = "lender")
public class Lender extends Person {

    public static Lender createWithRequiredFields (String firstname, String surname, String email){
        return new Lender(firstname, surname, email);
    }

    public Lender(String firstname, String surname, String email) {
        super(firstname, surname, email);
    }
}
