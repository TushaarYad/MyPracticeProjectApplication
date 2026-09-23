package com.tushaar.MyPracticeProject.service;

import com.tushaar.MyPracticeProject.config.JwtUtil;
import com.tushaar.MyPracticeProject.dto.AuthRequest;
import com.tushaar.MyPracticeProject.exception.EmployeeNotFoundException;
import com.tushaar.MyPracticeProject.exception.InvalidEmployeeException;
import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.repository.EmployeeRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    //Hmm call the following beans
    private final EmployeeRepo empRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; //Creation of tokens

    //Constructor Injection
    public AuthService(EmployeeRepo empRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.empRepo = empRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //AuthRequest In DTO containing username and passwords
    public String login (AuthRequest request ) {
        //First look up if the name exists
        //Since it is expecting an employee, any means no employee registered as this, preventing a null return
        Employee emp = empRepo.findByName(request.getUsername())
                .orElseThrow(()-> new EmployeeNotFoundException(request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), emp.getPassword())) {
            // Same message as above! Never tell the client whether username or password was wrong.
            throw new InvalidEmployeeException("Invalid credentials");
        }
        return jwtUtil.generateToken(emp.getName());
    }
}
