package com.example.hrm.controller;

import com.example.hrm.dto.request.EmployeeRequest;
import com.example.hrm.dto.response.EmployeeResponse;
import com.example.hrm.service.EmployeeService;
import com.example.hrm.service.ProfileService;
import com.example.hrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ProfileService profileService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public EmployeeResponse getEmployee(@PathVariable Long id){
        return employeeService.getEmployee(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public EmployeeResponse saveEmployee(@RequestBody EmployeeRequest employeeRequest){
        return employeeService.saveEmployee(employeeRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public EmployeeResponse updateEmployee(@PathVariable Long id,@RequestBody EmployeeRequest employeeRequest){
        return employeeService.updateEmployee(id,employeeRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/{employeeId}/link-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> linkUserToEmployee(
            @PathVariable Long employeeId,
            @RequestBody Map<String, Long> request) { // Dùng Map cho nhanh, khỏi tạo DTO mới

        Long userId = request.get("userId");
        profileService.linkUser(employeeId, userId);

        return ResponseEntity.ok("Đã liên kết tài khoản thành công với nhân viên!");
    }
}
