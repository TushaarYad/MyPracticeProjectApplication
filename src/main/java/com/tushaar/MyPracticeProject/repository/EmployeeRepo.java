package com.tushaar.MyPracticeProject.repository;

import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
ConcurrentHashMap


Stuff that I used here
1. List -> stores a list of objects without an end unlike array. It is varying in size unlike normal arrays. Can contain zero stuff or more.
2. HashMap -> allows us to retrieve an employee by id indicated by the integer in the <>
3. final -> indicates a constant
4. .map(), is used to transform each element into something else. .map(Employee::getName) for every Employee in this stream, call getName() and use the result.
5. .filter() is used to keep only the elements that satisfy a condition.
6. Optional is a container object used as a return type to explicitly indicate that a method may or may not return a value, helping prevent NullPointerExceptions. Search = one or more.
*/


/*
Optional.ofNullable returns an Optional containing the specified value if it is non-null,
or an empty Optional if the value is null, preventing a NullPointerException.
In the method, if employees.get(id) finds an Employee, it wraps that object; if it returns null
(e.g., the key/ID doesn't exist), it safely wraps null as Optional.empty().
*/


/*
Lambda Expression

Structure: (parameter) -> (expression)

Example:
employee -> employee.getId() == id
   ↑                    ↑
parameter            expression

Normal method
public boolean checkEmployee(Employee employee, int id) {
    return employee.getId() == id;
}

Lambda Expression
employee -> employee.getId() == id;



PUT - Replaces old data to new one
We had to make 2 decisions here:
1. Can the employee type be changed? For now let us assume yes and delete the old data.
2. What will happen to the old password? The safest version: if password is null in the request, keep the existing one; otherwise re-hash.


searchEmpRepo
if (type == null) {
    return true;
}
If the user didn't specify a type (i.e., type is null), it returns true for everyone.

String actualType = (e instanceof FullTimeEmployee ? "FULL TIME" : "PART TIME");
If e is an instance of FullTimeEmployee, actualType becomes "FULL TIME". Otherwise, actualType becomes "PART TIME".

return actualType.equalsIgnoreCase(type);
It compares the actualType label to the type parameter supplied to the method, ignoring uppercase or lowercase differences. Could've convert to upper or lower.
*/


//@Repository
//marks a class as a Data Access Object (DAO)
//to handle database operations and automatically translates low-level database exceptions into Spring's unified data access exceptions.
@Repository
public class EmployeeRepo {
    /*Since we can fetch users by id we will use a hashmap instead
    private final List<employee> employees = new ArrayList<>();*/

    private final HashMap<Integer, Employee> employees = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    //We will use password encoder here too
    private final PasswordEncoder passwordEncoder;

    public EmployeeRepo() {
        //Inject password encoder
        this.passwordEncoder = new BCryptPasswordEncoder();

        // seed data — use nextId so we don't collide later
        int id1 = nextId.getAndIncrement();
        employees.put(id1,
                new FullTimeEmployee(
                        id1,
                        "John",
                        passwordEncoder.encode("password123"),
                        "john.jpg",
                        45000
                )
        );
        int id2 = nextId.getAndIncrement();
        employees.put(id2,
                new PartTimeEmployee(
                        id2,
                        "Sarah",
                        passwordEncoder.encode("password456"),
                        "sarah.jpg",
                        85
                )
        );
        int id3 = nextId.getAndIncrement();
        employees.put(id3,
                new FullTimeEmployee(
                        id3,
                        "Michael",
                        passwordEncoder.encode("mikeSecure1"),
                        "michael.jpg",
                        52000
                )
        );
        int id4 = nextId.getAndIncrement();
        employees.put(id4,
                new PartTimeEmployee(
                        id4,
                        "Emily",
                        passwordEncoder.encode("emilyPass22"),
                        "emily.jpg",
                        92
                )
        );
        int id5 = nextId.getAndIncrement();
        employees.put(id5,
                new FullTimeEmployee(
                        id5,
                        "David",
                        passwordEncoder.encode("davidStrong3"),
                        "david.jpg",
                        61000
                )
        );
        int id6 = nextId.getAndIncrement();
        employees.put(id6,
                new PartTimeEmployee(
                        id6,
                        "Jessica",
                        passwordEncoder.encode("jessica789"),
                        "jessica.jpg",
                        78
                )
        );
        int id7 = nextId.getAndIncrement();
        employees.put(id7,
                new FullTimeEmployee(
                        id7,
                        "Robert",
                        passwordEncoder.encode("robPass456"),
                        "robert.jpg",
                        48000
                )
        );
        int id8 = nextId.getAndIncrement();
        employees.put(id8,
                new PartTimeEmployee(
                        id8,
                        "Amanda",
                        passwordEncoder.encode("amandaKey99"),
                        "amanda.jpg",
                        95
                )
        );
        int id9 = nextId.getAndIncrement();
        employees.put(id9,
                new FullTimeEmployee(
                        id9,
                        "Christopher",
                        passwordEncoder.encode("chrisSecure7"),
                        "chris.jpg",
                        57500
                )
        );
        int id10 = nextId.getAndIncrement();
        employees.put(id10,
                new PartTimeEmployee(
                        id10,
                        "Lauren",
                        passwordEncoder.encode("laurenPass33"),
                        "lauren.jpg",
                        88
                )
        );
    }

    //Incrementing the Id
    public int getNextId() {
        return nextId.getAndIncrement();
    }

    //Here the method findAllRepo shall return a LIST of employees to the service
    public List<Employee> findAllRepo(){
        return new ArrayList<>(employees.values());
    }

    //Here the method findByIdRepo will return one employee based on its id
    public Optional<Employee> findByIdRepo(int id){
        return Optional.ofNullable(employees.get(id));
    }

    //Here the name of employee based on the id of that employee
    //.findFirst() already returns an Optional so it is not necessary to return Optional.ofNullable
    public Optional<String> findEmployeeNameByIdRepo(int id){
        return employees.values().stream()
                .filter(employee -> employee.getId() == id)
                .map(Employee::getName)
                .findFirst();
    }

    //Save items in the HashMap
    public Employee saveEmployeeRepo(Employee emp){
        employees.put(emp.getId(), emp);
        return emp;
    }

    //Searching by query in the link sent (Query Parameter @RequestParam)
    public List<Employee> searchEmpRepo(String name, String type) {
        return employees.values()
                .stream()
                //find all employees whose name contains the parameter if ever the name is not null
                .filter(e -> name == null || e.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(e -> {
                    if (type == null) {
                        return true;
                    }
                    String actualType = (e instanceof FullTimeEmployee ? "FULL TIME" : "PART TIME");
                    return actualType.equalsIgnoreCase(type);
                })
                .toList();
    }

    //Delete items in the HashMap
    public void deleteEmployeeByIdRepo(int id){
        employees.remove(id);
    }
}
