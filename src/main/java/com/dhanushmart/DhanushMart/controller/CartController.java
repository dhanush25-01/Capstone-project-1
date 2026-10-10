package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.model.*;
import com.dhanushmart.DhanushMart.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {
    private final CartService service;
    public CartController(CartService service){this.service=service;}

    private Customer customer(HttpSession s){return (Customer)s.getAttribute("customer");}

    @GetMapping("/cart")
    public String cart(HttpSession session,Model model){
        Customer c=customer(session);
        if(c==null)return "redirect:/login";
        Cart cart=service.getOrCreate(c.getId());
        model.addAttribute("cart",cart);
        model.addAttribute("total",service.total(cart));
        return "cart";
    }

    @PostMapping("/cart/add")
    public String add(@RequestParam Long productId,@RequestParam(defaultValue="1") int quantity,
                      HttpSession session,Model model){
        Customer c=customer(session); if(c==null)return "redirect:/login";
        try{service.add(c.getId(),productId,quantity);return "redirect:/cart";}
        catch(Exception e){return "redirect:/product/"+productId+"?error="+e.getMessage();}
    }

    @PostMapping("/cart/update")
    public String update(@RequestParam Long itemId,@RequestParam int quantity,HttpSession session){
        Customer c=customer(session);if(c==null)return "redirect:/login";
        try{service.update(c.getId(),itemId,quantity);}catch(Exception ignored){}
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long itemId,HttpSession session){
        Customer c=customer(session);if(c==null)return "redirect:/login";
        service.remove(c.getId(),itemId);return "redirect:/cart";
    }
}
