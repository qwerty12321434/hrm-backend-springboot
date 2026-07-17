package com.example.hrm.service;

import com.example.hrm.dto.request.EmployeeRequest;
import com.example.hrm.dto.response.EmployeeResponse;
import com.example.hrm.dto.response.ProfileResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.entity.Employee;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.repository.EmployeeRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeResponse getMyProfile(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Khong co username "+username));

        Employee employee = employeeRepository.findByUser(user);

        EmployeeResponse employeeResponse= new EmployeeResponse();
        employeeResponse.setFullName(employee.getFullName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setDepartment(employee.getDepartment());

        return employeeResponse;
    }

    public void linkUser(Long employeeId, Long userId) {
        // 1. Tìm Employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));

        // 2. Tìm User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        // (Tùy chọn) Kiểm tra xem User này đã bị gắn cho ai khác chưa
        // if (employeeRepository.existsByUser(user)) { throw ... }

        // 3. Móc nối và lưu lại
        employee.setUser(user);
        employeeRepository.save(employee);
    }
}
