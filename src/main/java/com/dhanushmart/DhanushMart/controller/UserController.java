package com.dhanushmart.DhanushMart.controller;

import com.dhanushmart.DhanushMart.model.User;
import com.dhanushmart.DhanushMart.repository.ProductRepository;
import com.dhanushmart.DhanushMart.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository repo;
    private final ProductRepository productRepo;

    public UserController(UserRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            if (repo.findByUsername(user.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setRole("USER");
            user.setStatus("PENDING");
            repo.save(user);
            return "redirect:/user/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User u = repo.findByUsername(username).orElse(null);

        if (u == null || !u.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }

        if (!"APPROVED".equalsIgnoreCase(u.getStatus())) {
            if ("REJECTED".equalsIgnoreCase(u.getStatus())) {
                model.addAttribute("error", "Your registration was rejected by the admin.");
            } else {
                model.addAttribute("error", "Your registration is waiting for admin approval.");
            }
            return "user/login";
        }

        session.setAttribute("user", u);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", u);
        model.addAttribute("productCount", productRepo.count());
        model.addAttribute("approvedUsers", repo.countByStatus("APPROVED"));
        model.addAttribute("pendingUsers", repo.countByStatus("PENDING"));
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", u);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
                                HttpSession session,
                                Model model) {
        User current = (User) session.getAttribute("user");
        if (current == null) {
            return "redirect:/user/login";
        }

        if (name == null || name.isBlank()) {
            model.addAttribute("error", "Name is required.");
            model.addAttribute("user", current);
            return "user/profile";
        }

        current.setName(name.trim());
        current.setEmail(email == null ? "" : email.trim());
        current.setPhone(phone == null ? "" : phone.trim());

        repo.save(current);
        session.setAttribute("user", current);

        model.addAttribute("user", current);
        model.addAttribute("success", "Profile updated successfully.");
        return "user/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", u);
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        User current = (User) session.getAttribute("user");
        if (current == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", current);

        if (!current.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "Current password is incorrect.");
            return "user/change-password";
        }

        if (newPassword == null || newPassword.length() < 6) {
            model.addAttribute("error", "New password must contain at least 6 characters.");
            return "user/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirmation do not match.");
            return "user/change-password";
        }

        current.setPassword(newPassword);
        repo.save(current);
        session.setAttribute("user", current);

        model.addAttribute("success", "Password changed successfully.");
        return "user/change-password";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
