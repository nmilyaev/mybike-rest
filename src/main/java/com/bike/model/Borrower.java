package com.bike.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "borrower")
@Table(name = "borrower")
public class Borrower extends Person {

    public static Borrower createWithRequiredFields (String firstname, String surname, String email){
        return new Borrower(firstname, surname, email);
    }

    public Borrower(String firstname, String surname, String email) {
        super(firstname, surname, email);
    }

}
