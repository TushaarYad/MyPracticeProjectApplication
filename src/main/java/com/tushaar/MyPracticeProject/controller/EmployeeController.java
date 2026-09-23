package com.tushaar.MyPracticeProject.controller;


import com.tushaar.MyPracticeProject.dto.EmployeePOSTRequest;
import com.tushaar.MyPracticeProject.dto.EmployeeGETResponse;
import com.tushaar.MyPracticeProject.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@Controller
serves user-facing HTML web pages through a view resolver,
whereas a @RestController skips web pages entirely and returns raw data like JSON or XML directly to the client.


@RestController
simplifies creating RESTful web services by automatically converting Java objects into HTTP responses like JSON or XML.

@RequestMapping
maps HTTP requests to specific handler methods or controller classes by defining the URL path, HTTP method, headers, and media types.

@GetMapping
is a Spring Boot annotation that maps HTTP GET requests onto specific handler methods to retrieve data.

@PathVariable
is a Spring Boot annotation used to extract dynamic values directly from the URI path into a method parameter.

@RequestParam
Query Parameters — the ?key=value in URLs
"Which ones / how?" → query parameter

*/

/*Constructor Injection
Constructor injection is a dependency injection pattern where a class receives its required dependencies as arguments
through its constructor at the exact moment it is instantiated.By forcing dependencies to be passed immediately during object creation,
it ensures the class is always fully initialized, promotes immutability (allowing fields to be declared final),
and makes unit testing much easier since dependencies can be directly mocked or passed in without relying on a Spring container.


DO NOT check for null values in the constructor because:

Scatters null checks across every endpoint.
Makes controllers know about repository semantics.
Duplicates the same error shape everywhere.
Easy to forget one endpoint.

Exceptions + @ControllerAdvice centralize the behavior in one place.

Authentication
My map
1. POST /auth/register  → creates an employee, returns nothing special
2. POST /auth/login     → validates credentials, returns a JWT
3. GET /employees/...   → requires "Authorization: Bearer <jwt>" header
4. JwtFilter intercepts every request, validates the token, sets the security context

*/

@RestController
@RequestMapping("/employees")


public class EmployeeController {

    private final EmployeeService empService;

    //Constructor Injection
    public EmployeeController(EmployeeService empService) {
        this.empService = empService;
    }

    //Mapping to get all the employees in a JSON format
    @GetMapping("/allEmployees")
    public List<EmployeeGETResponse> getAllEmpController(){
        return empService.getAllEmpService();
    }

    //Mapping to get an employee based on his Id
    @GetMapping("/employeeById/{id}")
    public EmployeeGETResponse getEmpByIdController(@PathVariable int id){
        return empService.getEmpByIdService(id);
    }

    //Mapping to get an employee's name based on his Id
    @GetMapping("/employeeNameById/{id}")
    public String getEmpNameByIdController(@PathVariable int id){
        return empService.getEmpNameByIdService(id);
    }

    //Example of query parameter with two conditions
    @GetMapping("/search")
    public List<EmployeeGETResponse> searchEmpController(
            @RequestParam(required = false) String name, //tells Spring to inject the URL parameter name if present, or pass null if the client omits it from the request.
            @RequestParam(required = false) String type
            ){
        return empService.searchEmpService(name, type);
    }

    //Mapping to put something in the repository
    @PostMapping("/addEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeGETResponse saveEmployeeController(@Valid @RequestBody EmployeePOSTRequest emp){
        System.out.println("saveEmployeeController got as object: " +
                "name " + emp.getName() +
                ",  password " + emp.getPassword() +
                ", image " + emp.getImage() +
                ", salary " + emp.getSalary() +
                ", hourly rate " + emp.getHourlyRate() +
                ", type " + emp.getType());
        return empService.saveEmployeeService(emp);
    }

    @PutMapping("/updateEmployee/{id}")
    public EmployeeGETResponse updateEmployeeController(
            @PathVariable int id,
            @Valid @RequestBody EmployeePOSTRequest emp){
        return empService.updateEmployeeService(id, emp);
    }


    /*For deletion we need to choose a return status code since it cannot return an object. 204 No Content.
    The resource is gone — there's nothing to return. A body would be redundant.
    We shall handle ID does not exist and also if the operation is idempotent, meaning the second call should give 404 not found. */
    @DeleteMapping("/deleteEmployee/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeController(@PathVariable int id){
        empService.deleteEmployeeByIdRepo(id);
    }
}




