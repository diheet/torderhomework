package com.homework.torder.Controller;

import com.homework.torder.Dto.AllmenuDto;
import com.homework.torder.Dto.OrderMenuDto;
import com.homework.torder.Dto.PayDto;
import com.homework.torder.Service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {
    @Autowired
    MainService mainService;

    @RequestMapping("/index")
    public ModelAndView index () {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main/index");
        return mv;
    }

    @GetMapping("/menu")
    public @ResponseBody List<AllmenuDto> menu () {
        return mainService.getAllMenu();
    }

    @PostMapping("/order")
    public @ResponseBody String order(@RequestBody List<OrderMenuDto> menuCart) {
        String msg = "성공";
//        System.out.println("데이타 : "+menuCart.get(0).getMENU_NAME());
        mainService.setOrders(menuCart);
        return msg;
    }

    @GetMapping("/list")
    public @ResponseBody List<OrderMenuDto> list () {
        return mainService.getAlllist();
    }

    @PostMapping("/paylist")
    public @ResponseBody String paylist(@RequestBody List<PayDto> test) {
        String msg = "good";
//        System.out.println("데이터 : "+test.get(0).getMenu_name());
        mainService.setPays(test);
        mainService.deleteOrder();
        return msg;
    }
}
