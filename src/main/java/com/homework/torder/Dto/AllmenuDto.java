package com.homework.torder.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AllmenuDto {
    private String menu_code;
    private String menu_name;
    private int price;
}
