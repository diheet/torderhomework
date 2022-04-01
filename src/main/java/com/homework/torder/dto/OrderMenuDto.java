package com.homework.torder.dto;

import com.homework.torder.domain.OrderMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMenuDto {
    private String menu_name;
    private String menu_code;
    private int price;
    private int count;


    @Builder
    public OrderMenuDto (OrderMenu orderMenu){
        super();
        this.menu_code = orderMenu.getMenu_code();
        this.menu_name = orderMenu.getMenu_name();
        this.price = orderMenu.getPrice();
        this.count = orderMenu.getCount();
    }
}
