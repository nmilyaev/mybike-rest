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

}
