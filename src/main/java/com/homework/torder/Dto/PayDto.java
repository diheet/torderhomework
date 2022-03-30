package com.homework.torder.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PayDto {
    private String menu_name;
    private String menu_code;
    private int price;
    private int count;
}
