package com.example.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String fullName;

    private String email;

    private String department;

    private String phone;
}
