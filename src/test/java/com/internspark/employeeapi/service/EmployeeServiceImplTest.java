package com.internspark.employeeapi.service;

import com.internspark.employeeapi.dto.EmployeeRequest;
import com.internspark.employeeapi.dto.EmployeeResponse;
import com.internspark.employeeapi.entity.Employee;
import com.internspark.employeeapi.exception.DuplicateResourceException;
import com.internspark.employeeapi.exception.ResourceNotFoundException;
import com.internspark.employeeapi.repository.EmployeeRepository;
import com.internspark.employeeapi.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest request;
    private Employee entity;

    @BeforeEach
    void setUp() {
        request = EmployeeRequest.builder()
                .firstName("Asha")
                .lastName("Rao")
                .email("asha.rao@example.com")
                .department("Engineering")
                .salary(65000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12))
                .build();

        entity = Employee.builder()
                .id(1L)
                .firstName("Asha")
                .lastName("Rao")
                .email("asha.rao@example.com")
                .department("Engineering")
                .salary(65000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12))
                .build();
    }

    @Test
    void create_savesAndReturnsEmployee_whenEmailNotTaken() {
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(entity);

        EmployeeResponse response = employeeService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("asha.rao@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void create_throwsDuplicateResourceException_whenEmailAlreadyExists() {
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.create(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getById_returnsEmployee_whenFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));

        EmployeeResponse response = employeeService.getById(1L);

        assertThat(response.getFirstName()).isEqualTo("Asha");
    }

    @Test
    void getById_throwsResourceNotFoundException_whenMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_returnsAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(entity));

        List<EmployeeResponse> responses = employeeService.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getEmail()).isEqualTo("asha.rao@example.com");
    }

    @Test
    void update_updatesFields_whenEmployeeExists() {
        EmployeeRequest updateRequest = EmployeeRequest.builder()
                .firstName("Asha")
                .lastName("Rao-Kulkarni")
                .email("asha.rao@example.com")
                .department("Product")
                .salary(70000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeRepository.findByEmail(updateRequest.getEmail())).thenReturn(Optional.of(entity));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeResponse response = employeeService.update(1L, updateRequest);

        assertThat(response.getLastName()).isEqualTo("Rao-Kulkarni");
        assertThat(response.getDepartment()).isEqualTo("Product");
    }

    @Test
    void update_throwsResourceNotFoundException_whenEmployeeMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(99L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_removesEmployee_whenExists() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.delete(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void delete_throwsResourceNotFoundException_whenMissing() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(employeeRepository, never()).deleteById(any());
    }
}
