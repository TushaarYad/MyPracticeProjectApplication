package com.tushaar.MyPracticeProject.model;

public class PartTimeEmployee extends Employee {
    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    private double hoursWorked;

    public PartTimeEmployee(int id, String name, String password, String profilePicture, double hoursWorked) {
        //Since it extends employee, it should call the constructor of the class super first and then create itself here
        super(id, name, password, profilePicture);
        System.out.println("partTimeEmployee Constructor: Full Time Employee was created with id: " + id + " and hours worked: " + hoursWorked);
        this.hoursWorked = hoursWorked;
    }
}
