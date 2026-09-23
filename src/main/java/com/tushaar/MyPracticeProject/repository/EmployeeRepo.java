package com.tushaar.MyPracticeProject.repository;

import com.tushaar.MyPracticeProject.model.Employee;
import com.tushaar.MyPracticeProject.model.EmployeeType;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.model.PartTimeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*JPA REPOSITORY
     That's it. JpaRepository gives you:
       save(Employee) → Employee
       findById(Integer) → Optional<Employee>
       findAll() → List<Employee>
       deleteById(Integer) → void
       existsById(Integer) → boolean
       count() → long
       ... and more


       In JPQL, you can't reference the discriminator column by its database name.
       JPQL operates on entity properties and classes, not table columns. employee_type is a column,
       not a property — same reason findByType failed earlier.
       So SELECT e FROM Employee e WHERE employee_type = FULL_TIME won't work

       Tho we can use @Query("SELECT e FROM Employee e WHERE TYPE(e) = FullTimeEmployee")
*/
@Repository
//Since JpaReposity will take over, we do not have to write any logic here so its just interface
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    //    @Query("SELECT e FROM Employee e WHERE " +
    //            "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Employee> findByNameContainingIgnoreCase(String name); //WHERE LOWER(name) LIKE '%ali%'

    //Since type is not in the table field, we shall query it directly
    @Query("SELECT e FROM FullTimeEmployee e")
    List<FullTimeEmployee> findAllFullTime (); //WHERE employee_type = 'FULL_TIME'

    @Query("SELECT e FROM PartTimeEmployee e")
    List<PartTimeEmployee> findAllPartTime ();

    Optional<Employee> findByName(String name);
}
