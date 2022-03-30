package com.homework.torder.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class FoodMenu {

    @Id
    @Column(nullable = false)
    private String menu_code;

    private String menu_name;
    private int price;

}
