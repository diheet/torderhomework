package com.homework.torder.dto;


import com.homework.torder.domain.Pay;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PayDto {
    private String menu_name;
    private String menu_code;
    private int price;
    private int count;

    public PayDto (){

    }

    @Builder
    public PayDto (Pay pay){
        super();
        this.menu_code = pay.getMenu_code();
        this.menu_name = pay.getMenu_name();
        this.price = pay.getPrice();
        this.count = pay.getCount();
    }

}
