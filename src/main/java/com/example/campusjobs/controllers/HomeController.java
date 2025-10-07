package com.example.campusjobs.controllers;

import com.example.campusjobs.repositories.JobRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final JobRepository jobRepo;
    public HomeController(JobRepository jobRepo) { this.jobRepo = jobRepo; }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("jobs", jobRepo.findAll());
        return "index";
    }
}
