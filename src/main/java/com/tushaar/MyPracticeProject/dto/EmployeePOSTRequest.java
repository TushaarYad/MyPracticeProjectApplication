package com.tushaar.MyPracticeProject.dto;

import com.tushaar.MyPracticeProject.model.EmployeeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EmployeePOSTRequest {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @NotBlank(message = "Employee Name is required")
    @Size(min=2, max=50, message = "Employee name should be between 2 and 50 characters")
    private String name;

    @Size(min=2, message = "Employee password should be at least 2 characters")
    private String password;

    @NotBlank(message="Image is required")
    private String image;

    @NotNull(message = "Type is needed")
    private EmployeeType type;

    @Positive(message = "Salary must be positive ")
    private Double salary;

    @Positive(message = "Hourly Rate must be positive ")
    private Double hourlyRate;

    //Deserialize
    public EmployeePOSTRequest(){}


}
