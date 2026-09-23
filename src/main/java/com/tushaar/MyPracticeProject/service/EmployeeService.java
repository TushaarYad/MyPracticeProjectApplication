package com.tushaar.MyPracticeProject.service;

import com.tushaar.MyPracticeProject.dto.EmployeeGETResponse;
import com.tushaar.MyPracticeProject.dto.EmployeePOSTRequest;
import com.tushaar.MyPracticeProject.exception.EmployeeNotFoundException;
import com.tushaar.MyPracticeProject.exception.InvalidEmployeeException;
import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.model.EmployeeType;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;
import com.tushaar.MyPracticeProject.repository.EmployeeRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
DTO - Mapping DTOs is done here to keep the Service clean

public EmployeeResponse getEmpByIdService(int id) explained
.stream()
Converts the returned List<Employee> into a Java Stream.
This enables functional, declarative operations (like mapping or filtering) on the collection elements sequentially.

.map(EmployeeResponse::from)
Applies the map operation to transform each Employee object into an EmployeeResponse DTO.
EmployeeResponse::from is a method reference pointing to a static factory method inside the EmployeeResponse class.
It is a concise shorthand for: .map(employee -> EmployeeResponse.from(employee))

.toList()
Collects the stream results back into an unmodifiable List<EmployeeResponse> and
completes the stream pipeline (introduced in Java 16 as a simpler alternative to .collect(Collectors.toList())).


.from() explained
.from is a static factory method (or custom mapping method) defined inside your
EmployeeResponse class that takes a domain/entity object (Employee) and converts it into a Data Transfer Object (EmployeeResponse).


Since we introduced bean validation, we can clean validation in our service
*/
@Service
public class EmployeeService {

    private final EmployeeRepo empRepo;
    //For hashing passwords safely
    private final PasswordEncoder passwordEncoder;

    //Constructor Injection
    public EmployeeService(EmployeeRepo empRepo, PasswordEncoder passwordEncoder){
        this.empRepo = empRepo;
        //Inject the PasswordEncoder
        this.passwordEncoder = passwordEncoder;
    }

    //Helper class to create a new employee or to put existing one, elimination the repeated code to build one based on type
    private Employee buildEmployee(int id, String password, EmployeePOSTRequest emp) {

        //Create the new employee object that will be saved in the thing
        return switch (emp.getType()) {

            case FULL_TIME -> {
                //If the mam is Full Time, he must have a monthly fixed salary
                if (emp.getSalary() == null)
                    throw new InvalidEmployeeException("Employee salary is required for a full time employee");
                yield new FullTimeEmployee(id,emp.getName(), password, emp.getImage(), emp.getSalary());
            }


            case PART_TIME -> {
                //If the mam is Part Time, he must have a hourly worked
                if (emp.getHourlyRate() == null)
                    throw new InvalidEmployeeException("Employee hourly rate is required for a part time employee");
                yield new PartTimeEmployee(id,emp.getName(), password, emp.getImage(), emp.getHourlyRate());
            }

        };

    }

    //Retrieve all the employee
    public List<EmployeeGETResponse> getAllEmpService() {
        return empRepo.findAll().stream()
                .map(EmployeeGETResponse::from)
                .toList();
    }

    //Retrieve a single employee based on his Id
    public EmployeeGETResponse getEmpByIdService(int id){
        Employee emp = empRepo.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
        return EmployeeGETResponse.from(emp);
    }

    //Retrieve a single employee's name based on his Id.
    public String getEmpNameByIdService(int id) {
        Employee emp = empRepo.findById(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
        return emp.getName();
    }

    //Save items in the employee hashMap
    public EmployeeGETResponse saveEmployeeService(EmployeePOSTRequest emp){

        //A bug is here cuh as if we are inserting a user without checking if the latter has entered something, problem, will throw 500 error code which is not right
        //Since @Size will treat null values as
        if (emp.getPassword() == null || emp.getPassword().isBlank()) {
            throw new InvalidEmployeeException("Password is required for a new employee");
        }

        String encodedPassword = passwordEncoder.encode(emp.getPassword());

        //Save to repo and return response to DTO to print as appropriate, if anything went wrong, it will not reach this point and handled by the exceptions
        return EmployeeGETResponse.from( empRepo.save(buildEmployee(0,encodedPassword, emp)));
    }

    //Helper class to find if an employee matches a particular type sent
    private boolean matchesType(Employee e, String type) {
        EmployeeType actualType = (e instanceof FullTimeEmployee)
                ? EmployeeType.FULL_TIME
                : EmployeeType.PART_TIME;
        return actualType.name().equalsIgnoreCase(type);
    }

    //Update an employee
    public EmployeeGETResponse updateEmployeeService(int id, EmployeePOSTRequest emp){

        //Check first if the employee exist
        Employee existingObjEmp = empRepo.findById((id))
                .orElseThrow(()-> new EmployeeNotFoundException(id));

        //Hibernate cannot change the type of employee so we hit a wall, let's throw exception if ever we encountered this
        //First let's see what data was sent
        EmployeeType existingType = (existingObjEmp instanceof FullTimeEmployee) ? EmployeeType.FULL_TIME : EmployeeType.PART_TIME;

        //Check both now if they match
        if(existingType != emp.getType()){
            throw new InvalidEmployeeException("Cannot change employee type! Employee type is " + existingType + " and you send " + emp.getType());
        }

        //Check if whether we want to keep the old password or to generate a new one
        String password = (emp.getPassword() == null || emp.getPassword().isEmpty())
                ? existingObjEmp.getPassword()
                : passwordEncoder.encode(emp.getPassword());

        return  EmployeeGETResponse.from(empRepo.save(buildEmployee(id, password, emp)));
    }

    //Method 1: Search by condition, filter from the service instead of the database
    public List<EmployeeGETResponse> searchEmpService(String name, String type) {
        return empRepo.findAll()
                .stream()
                //find all employees whose name contains the parameter if ever the name is not null
                .filter(e -> name == null || e.getName().toLowerCase().contains(name.toLowerCase()))
                //find all employees whose type matches with the parameter if ever it is not null
                .filter(e -> type == null || matchesType(e, type))
                .map(EmployeeGETResponse::from)
                .toList();
    }

    //Method 2: Search by condition, filter from the database itself
    public List<EmployeeGETResponse> searchEmpServiceTwo(String name, String type) {

        List<Employee> base;

        if (name != null && type != null) {
            //If all data name and type was sent properly, create a base holding the employees. Both ARE present indicated by &&
            base = switch (type.toUpperCase()) {
                //if the type got, converted to upper case, match any of these, call the repo.
                case "FULL_TIME" -> new ArrayList<>(empRepo.findAllFullTime());
                case "PART_TIME" -> new ArrayList<>(empRepo.findAllPartTime());
                default -> throw new InvalidEmployeeException("Unknown type: " + type);
            };
            // filter by name in memory (or add a proper derived method per type)
            base = base.stream()
                    .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        } else if (name != null) {
            //only name
            base = empRepo.findByNameContainingIgnoreCase(name);
        } else if (type != null) {
            //only type
            base = switch (type.toUpperCase()) {
                case "FULL_TIME" -> new ArrayList<>(empRepo.findAllFullTime());
                case "PART_TIME" -> new ArrayList<>(empRepo.findAllPartTime());
                default -> throw new InvalidEmployeeException("Unknown type: " + type);
            };
        } else {
            //Neither type nor name was in the url, call everyone lol
            base = empRepo.findAll();
        }

        //Put it in the form GET reponse and convert to list
        return base.stream().map(EmployeeGETResponse::from).toList();
    }

    //Delete a particular employee inside repo, place logic to check for existence first
    public void deleteEmployeeByIdRepo(int id){
        //Check if Employee exists
        if(empRepo.findById(id).isEmpty()){
            throw new EmployeeNotFoundException(id);
        }
        //If the thing above didn't throw then there should be someone here
        empRepo.deleteById(id);
    }
}
