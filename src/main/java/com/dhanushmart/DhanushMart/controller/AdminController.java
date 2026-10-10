package com.dhanushmart.DhanushMart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dhanushmart.DhanushMart.model.Admin;
import com.dhanushmart.DhanushMart.model.Order;
import com.dhanushmart.DhanushMart.model.Product;
import com.dhanushmart.DhanushMart.model.User;
import com.dhanushmart.DhanushMart.repository.AdminRepository;
import com.dhanushmart.DhanushMart.repository.CategoryRepository;
import com.dhanushmart.DhanushMart.repository.CustomerRepository;
import com.dhanushmart.DhanushMart.repository.OrderRepository;
import com.dhanushmart.DhanushMart.repository.ProductRepository;
import com.dhanushmart.DhanushMart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public AdminController(AdminRepository adminRepo, ProductRepository productRepo,
                           CategoryRepository categoryRepo, CustomerRepository customerRepo,
                           OrderRepository orderRepo, UserRepository userRepo){
        this.adminRepo=adminRepo; this.productRepo=productRepo; this.categoryRepo=categoryRepo;
        this.customerRepo=customerRepo; this.orderRepo=orderRepo; this.userRepo=userRepo;
    }

    private boolean logged(HttpSession s){return s.getAttribute("admin")!=null;}

    @GetMapping("/login") public String login(){return "admin/login";}

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,@RequestParam String password,
                          HttpSession session,Model model){
        Admin a=adminRepo.findByUsername(username).orElse(null);
        if(a==null || !a.getPassword().equals(password)){
            model.addAttribute("error","Invalid admin login");
            return "admin/login";
        }
        session.setAttribute("admin",a);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){session.removeAttribute("admin");return "redirect:/admin/login";}

    @GetMapping("/dashboard")
    public String dashboard(HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("productCount",productRepo.count());
        m.addAttribute("customerCount",customerRepo.count());
        m.addAttribute("orderCount",orderRepo.count());
        m.addAttribute("userCount",userRepo.count());
        m.addAttribute("pendingUserCount",userRepo.countByStatus("PENDING"));
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products(HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("products",productRepo.findAll());
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String addPage(HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("product",new Product());
        m.addAttribute("categories",categoryRepo.findAll());
        return "admin/add-product";
    }

    @PostMapping("/products/save")
    public String save(@ModelAttribute Product form,@RequestParam Long categoryId,HttpSession s){
        if(!logged(s))return "redirect:/admin/login";
        Product product;
        if(form.getId() != null) {
            product = productRepo.findById(form.getId()).orElseThrow();
            // Preserve the owner if an admin edits a seller-owned product.
            product.setName(form.getName());
            product.setDescription(form.getDescription());
            product.setPrice(form.getPrice());
            product.setSize(form.getSize());
            product.setColor(form.getColor());
            product.setStock(form.getStock());
            product.setImage(form.getImage());
        } else {
            product = form;
        }
        product.setCategory(categoryRepo.findById(categoryId).orElseThrow());
        productRepo.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable Long id,HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("product",productRepo.findById(id).orElseThrow());
        m.addAttribute("categories",categoryRepo.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id,HttpSession s){
        if(!logged(s))return "redirect:/admin/login";
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }

    // User registration is public; only this admin area can approve/reject it.
    @GetMapping("/users")
    public String users(HttpSession s, Model m){
        if(!logged(s)) return "redirect:/admin/login";
        m.addAttribute("users", userRepo.findAll());
        m.addAttribute("pendingCount", userRepo.countByStatus("PENDING"));
        return "admin/users";
    }

    @PostMapping("/users/approve/{id}")
    public String approveUser(@PathVariable Long id, HttpSession s){
        if(!logged(s)) return "redirect:/admin/login";
        User user=userRepo.findById(id).orElseThrow();
        user.setStatus("APPROVED");
        userRepo.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/reject/{id}")
    public String rejectUser(@PathVariable Long id, HttpSession s){
        if(!logged(s)) return "redirect:/admin/login";
        User user=userRepo.findById(id).orElseThrow();
        user.setStatus("REJECTED");
        userRepo.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession s){
        if(!logged(s)) return "redirect:/admin/login";
        userRepo.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/customers")
    public String customers(HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("customers",customerRepo.findAll());
        return "admin/customers";
    }

    @GetMapping("/orders")
    public String orders(HttpSession s,Model m){
        if(!logged(s))return "redirect:/admin/login";
        m.addAttribute("orders",orderRepo.findAll());
        return "admin/orders";
    }

    @PostMapping("/orders/status/{id}")
    public String status(@PathVariable Long id,@RequestParam String status,HttpSession s){
        if(!logged(s))return "redirect:/admin/login";
        Order o=orderRepo.findById(id).orElseThrow();
        o.setStatus(status);
        orderRepo.save(o);
        return "redirect:/admin/orders";
    }
}
