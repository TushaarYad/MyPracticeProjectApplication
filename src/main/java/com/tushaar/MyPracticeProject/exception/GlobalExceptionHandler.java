package com.tushaar.MyPracticeProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
@RestControllerAdvice Explained
Acts as an interceptor for exceptions thrown by any @RestController across your application.
Combines @ControllerAdvice and @ResponseBody, meaning response objects returned by the methods inside
are automatically converted directly into JSON (or XML).

@ExceptionHandler(EmployeeNotFoundException.class)
Tells Spring to route any unhandled EmployeeNotFoundException
thrown anywhere in the controller layer to this specific method (handleNotFound).
The thrown exception instance (ex) is passed in as an argument so you can read its message or stack trace.

ProblemDetail
Introduced in Spring 6 / Spring Boot 3, ProblemDetail is a standardized format for reporting API errors.
ProblemDetail.forStatusAndDetail(...) initializes the payload with:
HTTP Status Code: 404 NOT_FOUND
Detail: The error message passed from the exception (ex.getMessage()).
problem.setTitle(...) sets a short human-readable summary of the error type.

ResponseEntity<ProblemDetail>
Wraps the ProblemDetail payload alongside explicit HTTP headers and status code (404 NOT_FOUND).
Generates a clean JSON response body for the client looking like this:
{
  "type": "about:blank",
  "title": "Employee Not Found",
  "status": 404,
  "detail": "Employee with ID 5 not found",
  "instance": "/employees/5"
}
*/

/*
Repository throws exception -> bubbles up.
Service throws exception -> bubbles up.
Controller throws exception -> bubbles up.
DispatcherServlet catches it -> looks for @RestControllerAdvice Explained -> finds your handler -> sends HTTP 404.
*/

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Handle Exception if no data was found
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(EmployeeNotFoundException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
        problem.setTitle("Employee Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    //Handle Exception for data sent by client
    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<ProblemDetail> handleInvalidEmployee(InvalidEmployeeException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );
        problem.setTitle("Invalid Employee Data Sent");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
}
