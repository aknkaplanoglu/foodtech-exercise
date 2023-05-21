package com.foodtech.exercise.service;

import com.foodtech.exercise.dto.request.EmployeeRequest;
import com.foodtech.exercise.exception.ResourceNotFoundException;
import com.foodtech.exercise.model.Department;
import com.foodtech.exercise.model.Employee;
import com.foodtech.exercise.repository.DepartmentRepository;
import com.foodtech.exercise.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee em1;
    private Employee em2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        em1 = new Employee();
        em1.setId(1L);
        em1.setFirstName("John");
        em1.setLastName("Doe");
        em1.setDepartment(new Department());

        em2 = new Employee();
        em2.setId(2L);
        em2.setFirstName("Jane");
        em2.setLastName("Smith");
        em2.setDepartment(new Department());
    }

    @Test
    void testFindAll() {
        // Prepare
        List<Employee> employees = new ArrayList<>();

        employees.add(em1);
        employees.add(em2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // Execute
        List<Employee> result = employeeService.findAll();

        // Verify
        assertEquals(employees.size(), result.size());
        assertEquals(employees, result);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWithPageable() {
        // Prepare
        Pageable pageable = Pageable.unpaged();
        Page<Employee> employeePage = mock(Page.class);

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        // Execute
        Page<Employee> result = employeeService.findAll(pageable);

        // Verify
        assertSame(employeePage, result);
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSave() {
        // Prepare
        Long departmentId = 1L;
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");

        Department department = new Department();
        department.setId(departmentId);

        Employee employee = new Employee();
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setDepartment(department);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Execute
        Employee result = employeeService.save(departmentId, employeeRequest);

        // Verify
        assertNotNull(result);
        assertEquals(employee.getFirstName(), result.getFirstName());
        assertEquals(employee.getLastName(), result.getLastName());
        assertEquals(employee.getDepartment(), result.getDepartment());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testSaveNonExistingDepartment() {
        // Prepare
        Long departmentId = 1L;
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> employeeService.save(departmentId, employeeRequest));
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        // Prepare
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(em1));

        // Execute
        employeeService.delete(employeeId);

        // Verify
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(em1);
    }

    @Test
    void testDeleteNonExistingEmployee() {
        // Prepare
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(employeeId));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void testFindById() {
        // Prepare
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(em1));

        // Execute
        Employee result = employeeService.findById(employeeId);

        // Verify
        assertNotNull(result);
        assertEquals(em1, result);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void testFindNonExistingEmployeeById() {
        // Prepare
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(employeeId));
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void testUpdateEmployee() {
        // Prepare
        Long employeeId = 1L;
        Long departmentId = 2L;
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setId(employeeId);
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setDepartmentId(departmentId);

        Department department = new Department();
        department.setId(departmentId);


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(em1));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(employeeRepository.save(em1)).thenReturn(em1);

        // Execute
        Employee result = employeeService.updateEmployee(employeeRequest);

        // Verify
        assertNotNull(result);
        assertEquals(em1.getFirstName(), result.getFirstName());
        assertEquals(em1.getLastName(), result.getLastName());
        assertEquals(em1.getDepartment(), result.getDepartment());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, times(1)).save(em1);
    }

    @Test
    void testUpdateNonExistingEmployee() {
        // Prepare
        Long employeeId = 1L;
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setId(employeeId);
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setDepartmentId(2L);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(employeeRequest));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(departmentRepository, never()).findById(any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployeeWithNonExistingDepartment() {
        // Prepare
        Long employeeId = 1L;
        Long departmentId = 2L;
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setId(employeeId);
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setDepartmentId(departmentId);


        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(em1));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(employeeRequest));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(employeeRepository, never()).save(any());
    }
}

