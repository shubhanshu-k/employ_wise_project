import com.employwise.EmployeeDirectory.controllers.EmployeeController;
import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addEmployee_Success() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        when(employeeService.addNewEmployee(any())).thenReturn("1");

        ResponseEntity<String> response = employeeController.addEmployee(employeeRequest, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("1", response.getBody());
    }


    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = new ArrayList<>();
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }


    @Test
    void deleteEmployee_Success() {
        when(employeeService.deleteEmployeeById(anyString())).thenReturn(true);

        ResponseEntity<Void> response = employeeController.deleteEmployee("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateEmployee_Success() {
        when(employeeService.updateEmployeeById(anyString(), any())).thenReturn(true);

        ResponseEntity<Void> response = employeeController.updateEmployee("1", new EmployeeRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getEmployeeById_Success() {
        Employee employee = new Employee();
        when(employeeService.getEmployeeById(anyString())).thenReturn(Optional.of(employee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }



}
