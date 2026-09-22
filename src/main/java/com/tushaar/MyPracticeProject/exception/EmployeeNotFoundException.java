package com.tushaar.MyPracticeProject.exception;


/*
* Overloading
* Overloading the constructor to return the different type of message based on the exception thrown
* */
public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(int id){
        super("Employee with id " + id + " not found");
    }
    public EmployeeNotFoundException(String message) {
        super(message);
    }
    public EmployeeNotFoundException() {
        super("No employee found");
    }
}
