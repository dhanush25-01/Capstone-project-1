package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductController(ProductRepository productRepo,CategoryRepository categoryRepo){
        this.productRepo=productRepo;this.categoryRepo=categoryRepo;
    }

    @GetMapping("/products")
    public String products(@RequestParam(required=false) String keyword,
                           @RequestParam(required=false) Long category,
                           Model model){
        if(keyword!=null && !keyword.isBlank())
            model.addAttribute("products",productRepo.findByNameContainingIgnoreCase(keyword));
        else if(category!=null)
            model.addAttribute("products",productRepo.findByCategoryId(category));
        else model.addAttribute("products",productRepo.findAll());
        model.addAttribute("categories",categoryRepo.findAll());
        model.addAttribute("keyword",keyword);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String details(@PathVariable Long id,Model model){
        model.addAttribute("product",productRepo.findById(id).orElseThrow());
        return "product-details";
    }
}
