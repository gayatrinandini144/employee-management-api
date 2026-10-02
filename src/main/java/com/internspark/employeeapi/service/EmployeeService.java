package com.internspark.employeeapi.service;

import com.internspark.employeeapi.dto.EmployeeRequest;
import com.internspark.employeeapi.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse getById(Long id);
    List<EmployeeResponse> getAll();
    EmployeeResponse update(Long id, EmployeeRequest request);
    void delete(Long id);
}
