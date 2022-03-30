package com.homework.torder.Domain;

import com.homework.torder.Dto.OrderMenuDto;
import com.homework.torder.Dto.PayDto;
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
public class Pay {
    @Id
    @Column(nullable = false)
    private String menu_code;

    private String menu_name;
    private int price;
    private int count;

    public Pay(PayDto payDto){
        this.menu_code = payDto.getMenu_code();
        this.menu_name = payDto.getMenu_name();
        this.price = payDto.getPrice();
        this.count = payDto.getCount();
    }
}
