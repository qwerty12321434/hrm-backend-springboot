package com.example.hrm.controller;

import com.example.hrm.dto.request.StatusRequest;
import com.example.hrm.dto.request.UserRequest;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse saveUser(@RequestBody UserRequest userRequest){
        return userService.saveUser(userRequest);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse setActiveUser(@PathVariable Long id, @RequestBody StatusRequest statusRequest){
        return userService.setActiveUser(id,statusRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
