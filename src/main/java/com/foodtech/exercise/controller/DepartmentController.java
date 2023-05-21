package com.foodtech.exercise.controller;

import com.foodtech.exercise.dto.request.DepartmentRequest;
import com.foodtech.exercise.model.Department;
import com.foodtech.exercise.service.DepartmentService;
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
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/departments/paginated")
    public ResponseEntity<Page<Department>> getAllDepartmentsWithPagination(Pageable pageable) {
        return ResponseEntity.ok(departmentService.findAll(pageable));
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentRequest department) {
        return ResponseEntity.ok(departmentService.save(department));
    }


    @PutMapping("/departments")
    public ResponseEntity<Department> updateDepartment(@RequestBody DepartmentRequest departmentDetails) {

        Department updatedDepartment = departmentService.updateDepartment(departmentDetails);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteDepartment(@PathVariable Long id) {

        departmentService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
