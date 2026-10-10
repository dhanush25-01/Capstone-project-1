package com.dhanushmart.DhanushMart.controller;
import com.dhanushmart.DhanushMart.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ProductRepository productRepo;
    public HomeController(ProductRepository productRepo){this.productRepo=productRepo;}

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("products",productRepo.findAll().stream().limit(8).toList());
        return "index";
    }
}
