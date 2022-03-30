package com.homework.torder.Domain;

import com.homework.torder.Dto.OrderMenuDto;
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
public class OrderMenu {
    @Id
    @Column(nullable = false)
    private String menu_code;

    private String menu_name;
    private int price;
    private int count;

    public OrderMenu(OrderMenuDto orderMenuDto){
        this.menu_code = orderMenuDto.getMenu_code();
        this.menu_name = orderMenuDto.getMenu_name();
        this.price = orderMenuDto.getPrice();
        this.count = orderMenuDto.getCount();
    }
}
