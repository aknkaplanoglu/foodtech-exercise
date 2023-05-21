/**
 * The EmployeeService class provides operations for managing employees.
 */
package com.foodtech.exercise.service;

import com.foodtech.exercise.dto.request.EmployeeRequest;
import com.foodtech.exercise.exception.ResourceNotFoundException;
import com.foodtech.exercise.model.Employee;
import com.foodtech.exercise.repository.DepartmentRepository;
import com.foodtech.exercise.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    /**
     * Retrieves all employees.
     *
     * @return the list of all employees
     */
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Retrieves a page of employees.
     *
     * @param pageable the pageable information
     * @return a page of employees
     */
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    /**
     * Saves a new employee with the specified department ID.
     *
     * @param depId            the ID of the department the employee belongs to
     * @param employeeRequest  the employee request containing the employee details
     * @return the saved employee
     * @throws ResourceNotFoundException if the department does not exist with the specified ID
     */
    public Employee save(Long depId, EmployeeRequest employeeRequest) {
        return departmentRepository.findById(depId).map(department -> {
            Employee employee = new Employee();
            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setDepartment(department);
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new ResourceNotFoundException("Department does not exist with ID: " + depId));
    }

    /**
     * Deletes an employee by its ID.
     *
     * @param id the ID of the employee to delete
     * @throws ResourceNotFoundException if the employee does not exist
     */
    public void delete(Long id) {
        employeeRepository.delete(findById(id));
    }

    /**
     * Retrieves an employee by its ID.
     *
     * @param id the ID of the employee to retrieve
     * @return the employee with the specified ID
     * @throws ResourceNotFoundException if the employee does not exist
     */
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with ID: " + id));
    }

    /**
     * Updates the details of an employee.
     *
     * @param employeeDetails the employee request containing the updated details
     * @return the updated employee
     * @throws ResourceNotFoundException if the employee or the department does not exist
     */
    public Employee updateEmployee(EmployeeRequest employeeDetails) {
        Employee employee = findById(employeeDetails.getId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setDepartment(departmentRepository.findById(employeeDetails.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department does not exist with ID: " + employeeDetails.getDepartmentId())));
        return employeeRepository.save(employee);
    }

}
