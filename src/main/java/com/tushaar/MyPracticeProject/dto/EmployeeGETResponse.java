package com.tushaar.MyPracticeProject.dto;

import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.model.EmployeeType;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;

//Temporary DTO to demonstrate EmployeeResponse
public class EmployeeGETResponse {

    public EmployeeGETResponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

//    public void setPassword(String password) {
//        this.password = password;
//    }
//    public String getPassword() {
//        return password;
//    }

    private int id;
    private String name;
    private String image;
    private EmployeeType type;
    private Double salary;
    private Double hourlyRate;
    //private String password;

    //The mapper converts Employee → EmployeeResponse.
    public static EmployeeGETResponse from(Employee employee) {
        EmployeeGETResponse dto = new EmployeeGETResponse();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setImage(employee.getProfilePicture());
        //dto.setPassword(employee.getPassword());

        //Checking if it is an instance of either full or part
        if (employee instanceof FullTimeEmployee fte) {
            dto.type = EmployeeType.FULL_TIME;
            dto.salary = fte.getMonthlySalary();
        } else if (employee instanceof PartTimeEmployee pte) {
            dto.type = EmployeeType.PART_TIME;
            dto.hourlyRate = pte.getHourlyRate();
        }
        return dto;
    }

}
