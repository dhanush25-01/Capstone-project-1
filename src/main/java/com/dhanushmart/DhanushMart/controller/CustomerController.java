package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.model.Customer;
import com.dhanushmart.DhanushMart.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service){this.service=service;}

    @GetMapping("/register")
    public String registerPage(Model model){model.addAttribute("customer",new Customer());return "register";}

    @PostMapping("/register")
    public String register(@ModelAttribute Customer customer, Model model){
        try{service.register(customer); return "redirect:/login?registered";}
        catch(Exception e){model.addAttribute("error",e.getMessage());return "register";}
    }

    @GetMapping("/login")
    public String loginPage(){return "login";}

    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String password,
                        HttpSession session,Model model){
        Customer c=service.login(email,password);
        if(c==null){model.addAttribute("error","Invalid email or password");return "login";}
        session.setAttribute("customer",c);
        return "redirect:/products";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){session.invalidate();return "redirect:/";}
}
