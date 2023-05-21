/**
 * The DepartmentService class provides operations for managing departments.
 */
package com.foodtech.exercise.service;

import com.foodtech.exercise.dto.request.DepartmentRequest;
import com.foodtech.exercise.exception.ResourceNotFoundException;
import com.foodtech.exercise.model.Department;
import com.foodtech.exercise.repository.DepartmentRepository;
import com.foodtech.exercise.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    /**
     * Retrieves all departments.
     *
     * @return the list of all departments
     */
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    /**
     * Retrieves a page of departments.
     *
     * @param pageable the pageable information
     * @return a page of departments
     */
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    /**
     * Saves a new department.
     *
     * @param departmentRequest the department request containing the department details
     * @return the saved department
     */
    public Department save(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        return departmentRepository.save(department);
    }

    /**
     * Retrieves a department by its ID.
     *
     * @param id the ID of the department to retrieve
     * @return the department with the specified ID
     * @throws ResourceNotFoundException if the department does not exist
     */
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department does not exist with ID: " + id));
    }

    /**
     * Deletes a department by its ID.
     *
     * @param id the ID of the department to delete
     * @throws ResourceNotFoundException if the department does not exist
     */
    @Transactional
    public void delete(Long id) {
        if (departmentRepository.existsById(id)) {
            employeeRepository.updateEmployeesWithDepartmentIdToNull(id);
            departmentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Department does not exist with ID: " + id);
        }
    }

    /**
     * Updates the details of a department.
     *
     * @param departmentDetails the department request containing the updated details
     * @return the updated department
     * @throws ResourceNotFoundException if the department does not exist
     */
    public Department updateDepartment(DepartmentRequest departmentDetails) {
        Department department = findById(departmentDetails.getId());
        department.setName(departmentDetails.getName());
        return departmentRepository.save(department);
    }
}
