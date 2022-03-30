package com.homework.torder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.homework.torder.dto.PayDto;
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
public class Pay {
    @Id
    @Column(nullable = false)
    private String menu_code;

    private String menu_name;
    private int price;
    private int count;

    @JsonIgnoreProperties({"pays"})
    @JoinColumn(name = "username")
    @ManyToOne
    private User user;

    public Pay(PayDto payDto, User user){
        this.menu_code = payDto.getMenu_code();
        this.menu_name = payDto.getMenu_name();
        this.price = payDto.getPrice();
        this.count = payDto.getCount();
        this.user = user;
    }
}
