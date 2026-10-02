package com.internspark.employeeapi.service.impl;

import com.internspark.employeeapi.dto.EmployeeRequest;
import com.internspark.employeeapi.dto.EmployeeResponse;
import com.internspark.employeeapi.entity.Employee;
import com.internspark.employeeapi.exception.DuplicateResourceException;
import com.internspark.employeeapi.exception.ResourceNotFoundException;
import com.internspark.employeeapi.repository.EmployeeRepository;
import com.internspark.employeeapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        log.info("Creating employee with email={}", request.getEmail());
        if (employeeRepository.existsByEmail(request.getEmail())) {
            log.warn("Attempted to create employee with duplicate email={}", request.getEmail());
            throw new DuplicateResourceException("Employee with email " + request.getEmail() + " already exists");
        }
        Employee saved = employeeRepository.save(toEntity(request));
        log.info("Created employee id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public EmployeeResponse getById(Long id) {
        log.debug("Fetching employee id={}", id);
        return employeeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> {
                    log.warn("Employee id={} not found", id);
                    return new ResourceNotFoundException("Employee not found with id " + id);
                });
    }

    @Override
    public List<EmployeeResponse> getAll() {
        log.debug("Fetching all employees");
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        log.info("Updating employee id={}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        employeeRepository.findByEmail(request.getEmail())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new DuplicateResourceException("Employee with email " + request.getEmail() + " already exists");
                });

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setDepartment(request.getDepartment());
        existing.setSalary(request.getSalary());
        existing.setDateOfJoining(request.getDateOfJoining());

        Employee updated = employeeRepository.save(existing);
        log.info("Updated employee id={}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting employee id={}", id);
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Deleted employee id={}", id);
    }

    private Employee toEntity(EmployeeRequest r) {
        return Employee.builder()
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .email(r.getEmail())
                .department(r.getDepartment())
                .salary(r.getSalary())
                .dateOfJoining(r.getDateOfJoining())
                .build();
    }

    private EmployeeResponse toResponse(Employee e) {
        return EmployeeResponse.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .department(e.getDepartment())
                .salary(e.getSalary())
                .dateOfJoining(e.getDateOfJoining())
                .build();
    }
}
