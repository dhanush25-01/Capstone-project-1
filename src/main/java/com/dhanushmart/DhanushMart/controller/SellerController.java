package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.model.Product;
import com.dhanushmart.DhanushMart.model.User;
import com.dhanushmart.DhanushMart.repository.CategoryRepository;
import com.dhanushmart.DhanushMart.repository.ProductRepository;
import com.dhanushmart.DhanushMart.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
public class SellerController {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public SellerController(UserRepository userRepo, ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    private User currentSeller(HttpSession session) {
        Object value = session.getAttribute("seller");
        if (!(value instanceof User)) return null;
        User seller = (User) value;
        if (!"SELLER".equalsIgnoreCase(seller.getRole()) || !"APPROVED".equalsIgnoreCase(seller.getStatus())) return null;
        return userRepo.findById(seller.getId()).orElse(null);
    }

    @GetMapping("/register")
    public String registerPage() { return "seller/register"; }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String username,
                           @RequestParam String password, @RequestParam(required = false) String email,
                           @RequestParam(required = false) String phone, Model model) {
        if (name.isBlank() || username.isBlank() || password.length() < 6) {
            model.addAttribute("error", "Enter your name and username, and use a password with at least 6 characters.");
            return "seller/register";
        }
        if (userRepo.findByUsername(username.trim()).isPresent()) {
            model.addAttribute("error", "That username already exists. Please choose another.");
            return "seller/register";
        }
        User seller = new User();
        seller.setName(name.trim());
        seller.setUsername(username.trim());
        seller.setPassword(password);
        seller.setEmail(email == null ? "" : email.trim());
        seller.setPhone(phone == null ? "" : phone.trim());
        seller.setRole("SELLER");
        seller.setStatus("PENDING");
        userRepo.save(seller);
        return "redirect:/seller/login?registered";
    }

    @GetMapping("/login")
    public String loginPage() { return "seller/login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User seller = userRepo.findByUsername(username).orElse(null);
        if (seller == null || !"SELLER".equalsIgnoreCase(seller.getRole()) || !seller.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid seller username or password.");
            return "seller/login";
        }
        if (!"APPROVED".equalsIgnoreCase(seller.getStatus())) {
            model.addAttribute("error", "Your seller account is " + seller.getStatus().toLowerCase() + ". Please wait for admin approval or contact the admin.");
            return "seller/login";
        }
        session.removeAttribute("user");
        session.setAttribute("seller", seller);
        return "redirect:/seller/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User seller = currentSeller(session);
        if (seller == null) { session.removeAttribute("seller"); return "redirect:/seller/login"; }
        model.addAttribute("seller", seller);
        model.addAttribute("products", productRepo.findBySeller_IdOrderByIdDesc(seller.getId()));
        model.addAttribute("productCount", productRepo.countBySeller_Id(seller.getId()));
        return "seller/dashboard";
    }

    @GetMapping("/products/add")
    public String addPage(HttpSession session, Model model) {
        if (currentSeller(session) == null) return "redirect:/seller/login";
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepo.findAll());
        return "seller/product-form";
    }

    @PostMapping("/products/save")
    public String save(@ModelAttribute Product form, @RequestParam Long categoryId, HttpSession session, Model model) {
        User seller = currentSeller(session);
        if (seller == null) return "redirect:/seller/login";
        Product product;
        if (form.getId() != null) {
            product = productRepo.findById(form.getId()).orElse(null);
            if (product == null || product.getSeller() == null || !product.getSeller().getId().equals(seller.getId())) {
                return "redirect:/seller/dashboard?error=notowner";
            }
        } else {
            product = new Product();
            product.setSeller(seller);
        }
        if (form.getName() == null || form.getName().isBlank() || form.getPrice() < 0 || form.getStock() < 0) {
            model.addAttribute("error", "Enter a product name and valid non-negative price and stock.");
            model.addAttribute("product", form);
            model.addAttribute("categories", categoryRepo.findAll());
            return "seller/product-form";
        }
        product.setName(form.getName().trim());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setSize(form.getSize());
        product.setColor(form.getColor());
        product.setStock(form.getStock());
        product.setImage(form.getImage());
        product.setCategory(categoryRepo.findById(categoryId).orElseThrow());
        product.setSeller(seller);
        productRepo.save(product);
        return "redirect:/seller/dashboard?saved";
    }

    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable Long id, HttpSession session, Model model) {
        User seller = currentSeller(session);
        if (seller == null) return "redirect:/seller/login";
        Product product = productRepo.findById(id).orElse(null);
        if (product == null || product.getSeller() == null || !product.getSeller().getId().equals(seller.getId())) return "redirect:/seller/dashboard?error=notowner";
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepo.findAll());
        return "seller/product-form";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User seller = currentSeller(session);
        if (seller == null) return "redirect:/seller/login";
        Product product = productRepo.findById(id).orElse(null);
        if (product != null && product.getSeller() != null && product.getSeller().getId().equals(seller.getId())) productRepo.delete(product);
        return "redirect:/seller/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.removeAttribute("seller"); return "redirect:/"; }
}
