package com.tushaar.MyPracticeProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

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





    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex){
    This method intercepts that exception, extracts all validation errors, and formats them into a clean, standardized HTTP 404/400 Bad Request JSON response.

    *Extracting Field Errors into a Map**********************************************************
    Map<String, String> fieldErrors =

        ex.getBindingResult()
        When Spring validates an incoming request (for example, via @Valid or @Validated), it stores the outcome in a BindingResult object.
        Think of BindingResult as Spring’s report card for the incoming data.
        It holds all validation outcome data, including which target object was validated, how many errors occurred, and details for each failure.


            .getFieldErrors()
            A validation error in Spring can be either a global error (affecting the entire object) or a field-level error (affecting a specific property, like email or age).
            .getFieldErrors() asks the report card for a List<FieldError> containing only errors attached to specific fields.
            Each FieldError object inside this list holds key details about the failure, such as:
            getField(): The name of the property that failed validation (e.g., "email").
            getDefaultMessage(): The validation message defined on the annotation (e.g., "must be a well-formed email address").
            getRejectedValue(): The actual invalid value sent by the user (e.g., "invalid-email-str").


            .stream()
            Converts the List<FieldError> (returned by getFieldErrors()) into a Java Stream. This allows processing each FieldError element sequentially using functional operations.

            .collect(
            A stream terminal operation that gathers all processed stream elements into a container—in this case, a Map.

            Collectors.toMap(
                    FieldError::getField,          // Key: The input field name (e.g., "email")
                    FieldError::getDefaultMessage,  // Value: The error message (e.g., "must not be blank")
                    (existing, replacement)->existing // Merge rule: Keep the first error if duplicates occur
            ));
            A built-in collector that takes up to three arguments to construct a Map:
            1. FieldError::getField (Key Mapper)
            Type: Method reference (equivalent to error -> error.getField())
            Role: Extracts the name of the invalid DTO property (e.g., "username", "email"). This string becomes the key in the target Map.

            2. FieldError::getDefaultMessage (Value Mapper)
            Type: Method reference (equivalent to error -> error.getDefaultMessage())
            Role: Extracts the human-readable validation error message associated with that field annotation (e.g., "must not be null"). This string becomes the value in the target Map

            3. (existing, replacement) -> existing (Merge Function)
                Type: Lambda expression taking two strings
                Role: Resolves duplicate key conflicts.
                Why it is necessary: If a single field violates multiple validation constraints (e.g., both @NotNull and @Size fail on username),
                Collectors.toMap will throw an IllegalStateException: Duplicate key without a merge function.
                Logic: When a second error for the same field key is encountered, (existing, replacement) ->
                existing tells Java to keep the existing (first) error message and discard the replacement (subsequent) error message.


            Collectors Function
               Collectors.toMap(
                        keyMapper,      // 1st argument: How to extract the Map Key for e.g email
                        valueMapper,    // 2nd argument: How to extract the Map Value e.g must not be blank
                        mergeFunction   // 3rd argument: What to do if two keys collide e.g
)                                       // Raw FieldErrors in the stream:
                                        // 1. field="email", message="must not be blank"
                                        // 2. field="email", message="must be a valid email"  <-- Duplicate key, in a map there shouldn't be duplicate keys. Remember Map<Key, Value>
                                        // 3. field="age",   message="must be 18 or older"

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

    //Handle errors if the @Valid throws them. It can throw multiples so we need to be careful here, maybe map?
    //MethodArgumentNotValidException used when @Valid throws an exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex){

        //Collect them all, as said before, can have multiple throws
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement)->existing //keep out duplicate values
                ));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields"
        );
        problem.setTitle("Validation Error");
        problem.setProperty("FieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    //Exception to handle bad enum type
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleUnreadable(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Malformed request or invalid value");
        problem.setTitle("Malformed Request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
}
