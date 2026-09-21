package com.tushaar.MyPracticeProject.service;

import com.tushaar.MyPracticeProject.dto.EmployeePOSTRequest;
import com.tushaar.MyPracticeProject.dto.EmployeeGETResponse;
import com.tushaar.MyPracticeProject.exception.EmployeeNotFoundException;
import com.tushaar.MyPracticeProject.exception.InvalidEmployeeException;
import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;
import com.tushaar.MyPracticeProject.repository.EmployeeRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
*/
@Service
public class EmployeeService {

    private final EmployeeRepo empRepo;
    //For hashing passwords safely
    private final PasswordEncoder pEncoder;
    private final PasswordEncoder passwordEncoder;

    //Constructor Injection
    public EmployeeService(EmployeeRepo empRepo, PasswordEncoder passwordEncoder){
        this.empRepo = empRepo;
        //Inject the PasswordEncoder
        this.pEncoder = new BCryptPasswordEncoder();
        this.passwordEncoder = passwordEncoder;
    }

    //Retrieve all the employee
    public List<EmployeeGETResponse> getAllEmpService() {
        return empRepo.findAllRepo().stream()
                .map(EmployeeGETResponse::from)
                .toList();
    }

    //Retrieve a single employee based on his Id
    public EmployeeGETResponse getEmpByIdService(int id){
        Employee emp = empRepo.findByIdRepo(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
        return EmployeeGETResponse.from(emp);
    }

    //Retrieve a single employee's name based on his Id
    public String getEmpNameByIdService(int id) {
        return empRepo.findEmployeeNameByIdRepo(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));
    }

    //Save items in the employee hashMap
    public EmployeeGETResponse saveEmployeeService(EmployeePOSTRequest emp){
        Employee objectEmp;

        //Validate first if type was specified
        if(emp.getType()==null){
            throw new InvalidEmployeeException("Employee type is required");
        }

        int newId = empRepo.getNextId();
        String encodedPassword = pEncoder.encode(emp.getPassword());

        //Create the new employee object that will be saved in the thing
        switch(emp.getType().toUpperCase()){

            case "FULL_TIME"-> {
                //If the mam is Full Time, he must have a monthly fixed salary
                if(emp.getSalary()== null) {
                    throw new InvalidEmployeeException("Employee salary is required for a full time employee");
                }
                //New object going
                 objectEmp = new FullTimeEmployee(
                        newId,
                        emp.getName(),
                         encodedPassword,
                        emp.getImage(),
                        emp.getSalary()
                );
            }

            case "PART_TIME"-> {
                //If the mam is Part Time, he must have a hourly worked
                if(emp.getHourlyRate()== null) {
                    throw new InvalidEmployeeException("Employee hourly rate is required for a part time employee");
                }
                objectEmp = new PartTimeEmployee(
                        newId,
                        emp.getName(),
                        encodedPassword,
                        emp.getImage(),
                        emp.getHourlyRate()
                );
            }

            //If for whatever reason part time or full time wasn't specified, then bruh throw a massive exception telling the cuh it ain't good
            default -> throw new InvalidEmployeeException("type must be FULL_TIME or PART_TIME, got: " + emp.getType());
        }

        //Save to repo and return response to DTO to print as appropriate, if anything went wrong, it will not reach this point and handled by the exceptions
        return EmployeeGETResponse.from( empRepo.saveEmployeeRepo(objectEmp));


    }

    //Update an employee
    public EmployeeGETResponse updateEmployeeService(int id, EmployeePOSTRequest emp){

        //Check first if the employee exist
        Employee existingObjEmp = empRepo.findByIdRepo(id)
                .orElseThrow(()-> new EmployeeNotFoundException(id));


        //Check if whether we want to keep the old password or to generate a new one
        String password = (emp.getPassword() == null || emp.getPassword().isEmpty())
                ? existingObjEmp.getPassword()
                : passwordEncoder.encode(existingObjEmp.getPassword());

        Employee updatedObjEmp;

        switch(emp.getType().toUpperCase()){
            case "FULL_TIME"-> {

                //If the mam is Full Time, he must have a monthly fixed salary
                if(emp.getSalary()== null) {
                    throw new InvalidEmployeeException("Employee salary is required for a full time employee");
                }

                //New object going
                updatedObjEmp = new FullTimeEmployee(
                        id,
                        emp.getName(),
                        password,
                        emp.getImage(),
                        emp.getSalary()
                );
            }
            case "PART_TIME"-> {
                if(emp.getHourlyRate()== null) {
                    throw new InvalidEmployeeException("Employee hourly rate is required for a part time employee");
                }
                        updatedObjEmp = new PartTimeEmployee(
                                id,
                                emp.getName(),
                                password,
                                emp.getImage(),
                                emp.getHourlyRate()
                        );

            }

            default -> throw new InvalidEmployeeException("type must be FULL_TIME or PART_TIME, got: " + emp.getType());
        }

        return  EmployeeGETResponse.from( empRepo.saveEmployeeRepo(updatedObjEmp));
    }

    //Search by condition
    public List<EmployeeGETResponse> searchEmpService(String name, String type) {
        return  empRepo.searchEmpRepo(name, type)
                .stream()
                .map(EmployeeGETResponse::from)
                .toList();
    }

    //Delete a particular employee inside repo, place logic to check for existence first
    public void deleteEmployeeByIdRepo(int id){
        //Check if Employee exists
        if(empRepo.findByIdRepo(id).isEmpty()){
            throw new EmployeeNotFoundException(id);
        }
        //If the thing above didn't throw then there should be someone here
        empRepo.deleteEmployeeByIdRepo(id);
    }

}
