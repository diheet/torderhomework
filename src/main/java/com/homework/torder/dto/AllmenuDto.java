package com.homework.torder.dto;

import com.homework.torder.domain.FoodMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllmenuDto {
    private String menu_code;
    private String menu_name;
    private int price;

    @Builder
    public AllmenuDto (FoodMenu foodMenu){
        super();
        this.menu_code = foodMenu.getMenu_code();
        this.menu_name = foodMenu.getMenu_name();
        this.price = foodMenu.getPrice();
    }
}
