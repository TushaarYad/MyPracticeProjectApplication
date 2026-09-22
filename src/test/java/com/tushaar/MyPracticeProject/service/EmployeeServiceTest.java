package com.tushaar.MyPracticeProject.service;

import com.tushaar.MyPracticeProject.dto.EmployeeGETResponse;
import com.tushaar.MyPracticeProject.exception.EmployeeNotFoundException;
import com.tushaar.MyPracticeProject.model.EmployeeType;
import com.tushaar.MyPracticeProject.model.FullTimeEmployee;
import com.tushaar.MyPracticeProject.repository.EmployeeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*

JUnit 5
@Test                      // marks a method as a test
@BeforeEach                // runs before each test (setup)
@AfterEach                 // runs after each test (cleanup)
@Disabled("reason")        // skip this test
@DisplayName("...")        // human-readable name in reports

Mockito
when(mock.method(arg)).thenReturn(value);          // stub a return
when(mock.method(any())).thenThrow(new Exception()); // stub a throw
verify(mock).method(arg);                          // assert it was called
verify(mock, never()).method(arg);                 // assert it was NOT called
verify(mock, times(2)).method(arg);                // called exactly twice
ArgumentCaptor<T> captor = ArgumentCaptor.forClass(T.class);

AssertJ
assertThat(value).isEqualTo(expected);
assertThat(value).isNull();
assertThat(value).isNotNull();
assertThat(list).hasSize(3);
assertThat(list).containsExactly(a, b, c);
assertThat(str).contains("sub");
assertThat(emp).isInstanceOf(FullTimeEmployee.class);

assertThatThrownBy(() -> method())
    .isInstanceOf(SomeException.class)
    .hasMessageContaining("something");
*/
@ExtendWith(MockitoExtension.class) //Tells JUnit hey cuh, activate Mockito for this class
class EmployeeServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder; //Create a fake instance of this type, since it is not very gucci to create new "real" objects for testing
    @Mock
    EmployeeRepo empRepo;
    @InjectMocks
    EmployeeService empService; //Create a real EmployeeService and inject the mocks (above the real cuh) into its constructor

    //Let's test the first one, getEmpById
    @Test
    void getEmpByIdService_returnsEmployee_whenFound() {
        //Arrange
        //Creating a fake full time employee just to test
        FullTimeEmployee john = new FullTimeEmployee(1, "John", "hashed", "john.jpg", 45000);
        //Telling the dude the empRepo if it is given id 1 then it should return John
        when(empRepo.findByIdRepo(1)).thenReturn(Optional.of(john)); //when the repo is asked for id 1, return this.

        //Act
        //Get the response back, if ever the thing was successful, it should get an object the same as created. Remember we use our DTO here*
        EmployeeGETResponse response = empService.getEmpByIdService(1);

        //Assert
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("John");
        assertThat(response.getType()).isEqualTo(EmployeeType.FULL_TIME);
        assertThat(response.getSalary()).isEqualTo(45000.0);
        assertThat(response.getHourlyRate()).isNull();

        verify(empRepo).findByIdRepo(1); //asserts the repo was actually called. Optional but useful.*
    }

    @Test
    void getEmpByIdService_returnsEmployee_whenNotFound() {
        //Arrange
        when(empRepo.findByIdRepo(99)).thenReturn(Optional.empty());

        //Act + Assert
        //AssertJ's way to assert an exception is thrown.
        assertThatThrownBy(() -> empService.getEmpByIdService(99))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("99"); //verifies the message is meaningful.
    }
}