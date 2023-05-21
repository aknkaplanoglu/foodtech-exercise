package com.foodtech.exercise.repository;

import com.foodtech.exercise.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);
    List<Employee> findByDepartmentId(Long departmentId);

    Optional<Employee> findByIdAndDepartmentId(Long id, Long departmentId);

    @Modifying
    @Query("UPDATE Employee e SET e.department = null WHERE e.department.id = :departmentId")
    void updateEmployeesWithDepartmentIdToNull(@Param("departmentId") Long departmentId);
}
