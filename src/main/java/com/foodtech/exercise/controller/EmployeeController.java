package com.foodtech.exercise.controller;

import com.foodtech.exercise.dto.request.EmployeeRequest;
import com.foodtech.exercise.model.Employee;
import com.foodtech.exercise.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/employees/paginated")
    public ResponseEntity<Page<Employee>> getAllEmployeesPaginated(Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAll(pageable));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    // create employee rest api
    @PostMapping("/departments/{departmentId}/employees")
    public ResponseEntity<Employee> createEmployee(@PathVariable(value = "departmentId") Long departmentId, @RequestBody EmployeeRequest employee) {
        return ResponseEntity.ok(employeeService.save(departmentId, employee));
    }

    // update employee rest api

    @PutMapping("/employees")
    public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeRequest employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    // delete employee rest api
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {

        employeeService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
