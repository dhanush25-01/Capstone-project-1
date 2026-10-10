package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.model.*;
import com.dhanushmart.DhanushMart.repository.OrderRepository;
import com.dhanushmart.DhanushMart.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepo;
    private final CartService cartService;

    public OrderController(OrderService orderService,OrderRepository orderRepo,CartService cartService){
        this.orderService=orderService;this.orderRepo=orderRepo;this.cartService=cartService;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session,Model model){
        Customer c=(Customer)session.getAttribute("customer");
        if(c==null)return "redirect:/login";
        Cart cart=cartService.getOrCreate(c.getId());
        if(cart.getItems().isEmpty())return "redirect:/cart";
        model.addAttribute("cart",cart);
        model.addAttribute("total",cartService.total(cart));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String address,HttpSession session,Model model){
        Customer c=(Customer)session.getAttribute("customer");
        if(c==null)return "redirect:/login";
        try{
            Order o=orderService.checkout(c,address);
            return "redirect:/orders?success="+o.getId();
        }catch(Exception e){
            model.addAttribute("error",e.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/orders")
    public String orders(HttpSession session,Model model){
        Customer c=(Customer)session.getAttribute("customer");
        if(c==null)return "redirect:/login";
        model.addAttribute("orders",orderRepo.findByCustomerIdOrderByOrderDateDesc(c.getId()));
        return "orders";
    }
}
