package com.homework.torder.Service;

import com.homework.torder.Domain.FoodMenu;
import com.homework.torder.Domain.OrderMenu;
import com.homework.torder.Domain.Pay;
import com.homework.torder.Dto.AllmenuDto;
import com.homework.torder.Dto.OrderMenuDto;
import com.homework.torder.Dto.PayDto;
import com.homework.torder.Repository.MenuRepository;
import com.homework.torder.Repository.OrderRepository;
import com.homework.torder.Repository.PayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PayRepository payRepository;

    @Transactional
    public List<AllmenuDto> getAllMenu() {
        List<FoodMenu> foodMenus = menuRepository.findAll();
        List<AllmenuDto> allmenuDtos = new ArrayList<>();

        for (FoodMenu foodMenu : foodMenus){
            AllmenuDto allmenuDto = AllmenuDto.builder()
                    .menu_code(foodMenu.getMenu_code())
                    .menu_name(foodMenu.getMenu_name())
                    .price(foodMenu.getPrice())
                    .build();

            allmenuDtos.add(allmenuDto);
        }
        return allmenuDtos;
    }

    public void setOrders(List<OrderMenuDto> orderList){
        for (OrderMenuDto orders: orderList) {
                OrderMenu orderMenu = new OrderMenu(orders);
                orderRepository.save(orderMenu);
        }
    }

    public List<OrderMenuDto> getAlllist() {
        List<OrderMenu> orderMenus = orderRepository.findAll();
        List<OrderMenuDto> orderMenuDtos = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus){
            OrderMenuDto orderMenuDto = OrderMenuDto.builder()
                    .menu_code(orderMenu.getMenu_code())
                    .menu_name(orderMenu.getMenu_name())
                    .price(orderMenu.getPrice())
                    .count(orderMenu.getCount())
                    .build();

            orderMenuDtos.add(orderMenuDto);
        }
        return orderMenuDtos;
    }

    public void setPays(List<PayDto> pays){
        for (PayDto payDto: pays){
            Pay pay = new Pay(payDto);
            payRepository.save(pay);
        }
    }
    public void deleteOrder() {
        orderRepository.deleteAll();
    }
}
