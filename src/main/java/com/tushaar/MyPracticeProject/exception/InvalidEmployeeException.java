package com.tushaar.MyPracticeProject.exception;

//Here used for post requests if ever data entered by client was invalid
public class InvalidEmployeeException extends RuntimeException{
    public InvalidEmployeeException(String message) {
        super(message);
    }
}
