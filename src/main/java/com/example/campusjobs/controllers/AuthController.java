package com.example.campusjobs.controllers;

import com.example.campusjobs.entities.Role;
import com.example.campusjobs.entities.User;
import com.example.campusjobs.repositories.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
public class AuthController {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder enc;

    public AuthController(UserRepository userRepo, BCryptPasswordEncoder enc) {
        this.userRepo = userRepo;
        this.enc = enc;
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm("", "", "STUDENT"));
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("form") @Validated RegisterForm form, Model model) {
        if (userRepo.findByEmail(form.email()).isPresent()) {
            model.addAttribute("error", "อีเมลนี้มีผู้ใช้แล้ว");
            return "register";
        }
        Role role = "STAFF".equalsIgnoreCase(form.role()) ? Role.STAFF : Role.STUDENT;
        User u = new User(form.email(), enc.encode(form.password()), role);
        userRepo.save(u);
        model.addAttribute("ok", "สมัครสมาชิกสำเร็จ ล็อคอินได้เลย");
        return "login";
    }

    public record RegisterForm(@Email String email, @NotBlank String password, @NotBlank String role) {}
}
