package com.foodtech.exercise.dto.request;

import lombok.Data;

@Data
public class EmployeeRequest {

    private long id;
    private String firstName;
    private String lastName;
    private long departmentId;
}
