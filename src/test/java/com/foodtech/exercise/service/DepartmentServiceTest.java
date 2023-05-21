package com.foodtech.exercise.service;

import com.foodtech.exercise.dto.request.DepartmentRequest;
import com.foodtech.exercise.exception.ResourceNotFoundException;
import com.foodtech.exercise.model.Department;
import com.foodtech.exercise.repository.DepartmentRepository;
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

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Prepare
        List<Department> departments = new ArrayList<>();
        Department dep1 = new Department();
        dep1.setId(1L);
        dep1.setName("Dep 1");
        departments.add(dep1);
        Department dep2 = new Department();
        dep2.setId(2L);
        dep2.setName("Dep2");
        departments.add(dep2);

        when(departmentRepository.findAll()).thenReturn(departments);

        // Execute
        List<Department> result = departmentService.findAll();

        // Verify
        assertEquals(departments.size(), result.size());
        assertEquals(departments, result);
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWithPageable() {
        // Prepare
        Pageable pageable = Pageable.unpaged();
        Page<Department> departmentPage = mock(Page.class);

        when(departmentRepository.findAll(pageable)).thenReturn(departmentPage);

        // Execute
        Page<Department> result = departmentService.findAll(pageable);

        // Verify
        assertSame(departmentPage, result);
        verify(departmentRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSave() {
        // Prepare
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("New Department");

        Department department = new Department();
        department.setName("New Department");

        when(departmentRepository.save(any())).thenReturn(department);

        // Execute
        Department result = departmentService.save(departmentRequest);

        // Verify
        assertNotNull(result);
        assertEquals(department.getName(), result.getName());
        verify(departmentRepository, times(1)).save(any());
    }

    @Test
    void testFindById() {
        // Prepare
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        department.setName("Dep 1");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // Execute
        Department result = departmentService.findById(departmentId);

        // Verify
        assertNotNull(result);
        assertEquals(department, result);
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testFindByIdNonExistingDepartment() {
        // Prepare
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> departmentService.findById(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void testDelete() {
        // Prepare
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        department.setName("Dep 1");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // Execute
        departmentService.delete(departmentId);

        // Verify
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void testDeleteNonExistingDepartment() {
        // Prepare
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> departmentService.delete(departmentId));
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, never()).delete(any());
    }

    @Test
    void testUpdateDepartment() {
        // Prepare
        long departmentId = 1L;
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(departmentId);
        departmentRequest.setName("Updated Department");

        Department department = new Department();
        department.setId(departmentId);
        department.setName("Dep 1");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);

        // Execute
        Department result = departmentService.updateDepartment(departmentRequest);

        // Verify
        assertNotNull(result);
        assertEquals(department.getId(), result.getId());
        assertEquals(departmentRequest.getName(), result.getName());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testUpdateNonExistingDepartment() {
        // Prepare
        Long departmentId = 1L;
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(departmentId);
        departmentRequest.setName("Updated Department");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(ResourceNotFoundException.class, () -> departmentService.updateDepartment(departmentRequest));
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, never()).save(any());
    }
}

