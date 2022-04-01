package com.homework.torder.controller;

import com.homework.torder.domain.User;
import com.homework.torder.dto.AllmenuDto;
import com.homework.torder.dto.OrderMenuDto;
import com.homework.torder.dto.PayDto;
import com.homework.torder.security.UserDetailsImpl;
import com.homework.torder.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
//로그인
    @RequestMapping("/login")
    public ModelAndView login () {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user/login");
        return mv;
    }

//메뉴가져오기
    @GetMapping("/menu")
    public @ResponseBody List<AllmenuDto> menu () {
        return mainService.getAllMenu();
    }

// 장바구니 -> 주문하기
    @PostMapping("/order")
    public @ResponseBody String order(@RequestBody List<OrderMenuDto> menuCart, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        String msg = "성공";
//        System.out.println("데이터 : "+menuCart.get(0).getMENU_NAME());
        mainService.setOrders(menuCart, user);
        return msg;
    }

//주문목록
    @GetMapping("/list")
    public @ResponseBody List<OrderMenuDto> list () {
        return mainService.getAlllist();
    }

//주문목록 -> 결제하기
    @PostMapping("/paylist")
    public @ResponseBody String paylist(@RequestBody List<PayDto> test, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        String msg = "good";
//        System.out.println("데이터 : "+test.get(0).getMenu_name());
        mainService.setPays(test, user);
        mainService.deleteOrder(user);
        return msg;
    }

//결제목록
    @GetMapping("/lastlist")
    public @ResponseBody List<PayDto> lastlist() {
        return mainService.getPaylist();
    }
}
