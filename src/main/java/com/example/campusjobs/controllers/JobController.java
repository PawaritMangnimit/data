package com.example.campusjobs.controllers;

import com.example.campusjobs.entities.Job;
import com.example.campusjobs.entities.User;
import com.example.campusjobs.repositories.JobRepository;
import com.example.campusjobs.repositories.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
public class JobController {

    private final JobRepository jobRepo;
    private final UserRepository userRepo;

    public JobController(JobRepository jobRepo, UserRepository userRepo) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/jobs/new")
    public String newJobForm(Model model) {
        model.addAttribute("form", new JobForm("", ""));
        return "job_form";
    }

    @PostMapping("/jobs")
    public String createJob(@ModelAttribute("form") @Validated JobForm form,
                            Authentication auth, Model model) {
        String email = auth.getName();
        User u = userRepo.findByEmail(email).orElseThrow();
        Job j = new Job(form.title(), form.description(), u);
        jobRepo.save(j);
        return "redirect:/";
    }

    public record JobForm(@NotBlank String title, @NotBlank String description) {}
}
