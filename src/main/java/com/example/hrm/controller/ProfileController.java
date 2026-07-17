package com.example.hrm.controller;

import com.example.hrm.dto.response.EmployeeResponse;
import com.example.hrm.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    public EmployeeResponse getMyProFile(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        String currentUsername = authentication.getName();

        return profileService.getMyProfile(currentUsername);
    }
}
