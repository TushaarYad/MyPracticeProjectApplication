package com.tushaar.MyPracticeProject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PART_TIME")
public class PartTimeEmployee extends Employee {
    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hoursWorked) {
        this.hourlyRate = hoursWorked;
    }

    private double hourlyRate;

    protected PartTimeEmployee() {}

    public PartTimeEmployee(int id, String name, String password, String profilePicture, double hourlyRate) {
        //Since it extends employee, it should call the constructor of the class super first and then create itself here
        super(id, name, password, profilePicture);
        System.out.println("partTimeEmployee Constructor: Full Time Employee was created with id: " + id + " and hours worked: " + hourlyRate);
        this.hourlyRate = hourlyRate;
    }
}
