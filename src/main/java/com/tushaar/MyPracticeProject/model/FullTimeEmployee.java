package com.tushaar.MyPracticeProject.model;


import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FULL_TIME")
public class FullTimeEmployee extends Employee {
    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    private double monthlySalary;

    protected FullTimeEmployee() {}

    public FullTimeEmployee(int id, String name, String password, String profilePicture, double monthlySalary) {
        //Since it extends employee, it should call the constructor of the class super first and then create itself here
        super(id, name, password, profilePicture);
        System.out.println("fullTimeEmployee Constructor: Full Time Employee was created with id: " + id + " and " + monthlySalary);
        this.monthlySalary = monthlySalary;
    }
}
