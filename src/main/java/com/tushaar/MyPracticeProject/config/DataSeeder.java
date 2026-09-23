package com.tushaar.MyPracticeProject.config;

import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;
import com.tushaar.MyPracticeProject.repository.EmployeeRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepo empRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(EmployeeRepo empRepo, PasswordEncoder passwordEncoder) {
        this.empRepo = empRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (empRepo.count() > 0) return;   // don't reseed on every restart

        empRepo.save(new FullTimeEmployee(0, "John", passwordEncoder.encode("password123"), "john.jpg", 45000));
        empRepo.save(new PartTimeEmployee(0, "Sarah", passwordEncoder.encode("password456"), "sarah.jpg", 85));
        empRepo.save(new FullTimeEmployee(0, "Michael", passwordEncoder.encode("mikeSecure1"), "michael.jpg", 52000));
        // ... rest of your seed employees
    }
}