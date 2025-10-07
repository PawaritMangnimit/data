package com.example.campusjobs.repositories;

import com.example.campusjobs.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {}
