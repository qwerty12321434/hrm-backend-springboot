package com.example.hrm.service;

import com.example.hrm.dto.request.StatusRequest;
import com.example.hrm.dto.request.UserRequest;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.repository.RoleRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserResponse> getAllUser(){
        List<User> users= userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user: users){
            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(user.getUsername());

            List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
            userResponse.setRoles(roleNames);

            userResponse.setActive(user.isActive());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public UserResponse saveUser(UserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        // lưu pass mã hóa
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        System.out.println(userRequest);
        for(String s:userRequest.getRoles()){
            s=s.toUpperCase();
            String roleName="";
            if(s.contains("MANAGER")) roleName="ROLE_MANAGER";
            else if(s.contains("ADMIN")) roleName="ROLE_ADMIN";
            else if (s.contains("EMPLOYEE")) roleName="ROLE_EMPLOYEE";
            else throw new RuntimeException("Không tồn tại role"+s);
            final String finalRoleName = roleName;
            Role existingRole = roleRepository.findByName(finalRoleName)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền " + finalRoleName + " trong Database"));
            roles.add(existingRole);

        }
        user.setRoles(roles);
        user.setActive(true);
        userRepository.save(user);

        return getUserResponse(user);
    }

    public UserResponse setActiveUser(Long id, StatusRequest statusRequest){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tồn tại id "+id+" này"));
        user.setActive(statusRequest.isStatus());
        userRepository.save(user);

        return getUserResponse(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @NonNull
    private UserResponse getUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setActive(user.isActive());
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        userResponse.setRoles(roleNames);

        return userResponse;
    }

}
