package com.internspark.employeeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.internspark.employeeapi.dto.EmployeeRequest;
import com.internspark.employeeapi.dto.EmployeeResponse;
import com.internspark.employeeapi.exception.ResourceNotFoundException;
import com.internspark.employeeapi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void create_returns201AndLocationHeader() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("Asha").lastName("Rao").email("asha.rao@example.com")
                .department("Engineering").salary(65000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12)).build();

        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L).firstName("Asha").lastName("Rao").email("asha.rao@example.com")
                .department("Engineering").salary(65000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12)).build();

        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsStringIgnoringCase("/api/v1/employees/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("asha.rao@example.com"));
    }

    @Test
    void create_returns400_whenPayloadInvalid() throws Exception {
        EmployeeRequest invalid = EmployeeRequest.builder()
                .firstName("").lastName("Rao").email("not-an-email")
                .department("Engineering").salary(-100.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12)).build();

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_returns200_whenFound() throws Exception {
        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L).firstName("Asha").lastName("Rao").email("asha.rao@example.com")
                .department("Engineering").salary(65000.0)
                .dateOfJoining(LocalDate.of(2023, 6, 12)).build();

        when(employeeService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Asha"));
    }

    @Test
    void getById_returns404_whenMissing() throws Exception {
        when(employeeService.getById(99L)).thenThrow(new ResourceNotFoundException("Employee not found with id 99"));

        mockMvc.perform(get("/api/v1/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id 99"));
    }

    @Test
    void delete_returns204_whenDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());
    }

    // small helper matcher since Hamcrest's isn't imported by default with this combo
    private static org.hamcrest.Matcher<String> containsStringIgnoringCase(String substring) {
        return org.hamcrest.Matchers.containsStringIgnoringCase(substring);
    }
}
