import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.repository.EmployeeRepository;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addEmployee_Success() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        when(employeeRepository.save(any())).thenReturn(new Employee());

        String employeeId = employeeService.addEmployee(employeeRequest);

        assertNotNull(employeeId);
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(employees, result);
    }

    @Test
    void deleteEmployeeById_Exists_Success() {
        String id = UUID.randomUUID().toString();
        when(employeeRepository.existsById(id)).thenReturn(true);

        boolean result = employeeService.deleteEmployeeById(id);

        assertTrue(result);
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void updateEmployeeById_Exists_Success() {
        String id = UUID.randomUUID().toString();
        Employee existingEmployee = new Employee();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));

        boolean result = employeeService.updateEmployeeById(id, new EmployeeRequest());

        assertTrue(result);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void getEmployeeById_Success() {
        String id = UUID.randomUUID().toString();
        Employee employee = new Employee();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(id);

        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

}
