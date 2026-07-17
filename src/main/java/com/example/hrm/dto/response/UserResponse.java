package com.example.hrm.dto.response;

import com.example.hrm.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private boolean isActive;
    private List<String> roles;
}
