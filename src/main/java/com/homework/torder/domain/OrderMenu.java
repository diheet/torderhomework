package com.homework.torder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.homework.torder.dto.OrderMenuDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @JsonIgnoreProperties({"orderMenus"})
    @JoinColumn(name = "username")
    @ManyToOne
    private User user;

    public OrderMenu(OrderMenuDto orderMenuDto, User user){
        this.menu_code = orderMenuDto.getMenu_code();
        this.menu_name = orderMenuDto.getMenu_name();
        this.price = orderMenuDto.getPrice();
        this.count = orderMenuDto.getCount();
        this.user = user;
    }
}
