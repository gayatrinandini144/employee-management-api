package com.internspark.employeeapi.dto;

import lombok.*;

import java.time.LocalDate;

/** Outgoing representation returned to API clients. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Double salary;
    private LocalDate dateOfJoining;
}
