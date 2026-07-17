package com.example.hrm.repository;


import com.example.hrm.entity.Employee;
import com.example.hrm.entity.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Employee findByUser(User user);
}
