package com.example.hrm.service;

import com.example.hrm.dto.request.EmployeeRequest;
import com.example.hrm.dto.response.EmployeeResponse;
import com.example.hrm.entity.Employee;
import com.example.hrm.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeResponse> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        for (Employee employee: employees){
            EmployeeResponse employeeResponse= new EmployeeResponse();
            employeeResponse.setFullName(employee.getFullName());
            employeeResponse.setEmail(employee.getEmail());
            employeeResponse.setPhone(employee.getPhone());
            employeeResponse.setDepartment(employee.getDepartment());

            employeeResponses.add(employeeResponse);
        }
        return employeeResponses;
    }

    public EmployeeResponse getEmployee(Long id){
        Employee employee= employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Khong ton tai employee id: "+id));
        EmployeeResponse employeeResponse= new EmployeeResponse();
        employeeResponse.setFullName(employee.getFullName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setDepartment(employee.getDepartment());
        return  employeeResponse;
    }

    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest){
        Employee employee = new Employee();
        employee.setFullName(employeeRequest.getFullName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhone(employeeRequest.getPhone());
        employee.setDepartment(employeeRequest.getDepartment());

        return getEmployeeResponse(employee);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("khong ton tai id employ"));
        if(!Objects.equals(employeeRequest.getFullName(), "")) employee.setFullName(employeeRequest.getFullName());
        if(!Objects.equals(employeeRequest.getDepartment(), "")) employee.setDepartment(employeeRequest.getDepartment());
        if(!Objects.equals(employeeRequest.getPhone(), "")) employee.setPhone(employeeRequest.getPhone());
        if(!Objects.equals(employeeRequest.getEmail(), "")) employee.setEmail(employeeRequest.getEmail());
        return getEmployeeResponse(employee);
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }


    @NonNull
    private EmployeeResponse getEmployeeResponse(Employee employee) {
        employeeRepository.save(employee);

        EmployeeResponse employeeResponse= null;
        try {
            employeeResponse = new EmployeeResponse();
            employeeResponse.setFullName(employee.getFullName());
            employeeResponse.setEmail(employee.getEmail());
            employeeResponse.setPhone(employee.getPhone());
            employeeResponse.setDepartment(employee.getDepartment());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return employeeResponse;
    }
}
