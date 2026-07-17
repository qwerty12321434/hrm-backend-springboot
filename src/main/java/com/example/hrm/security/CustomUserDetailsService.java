package com.example.hrm.security;

import com.example.hrm.entity.User;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null){
            throw new UsernameNotFoundException("Sai thông tin đăng nhập! không tìm thấy tài khoản "+username);
        }

        return new CustomUserDetails(user);
    }
}
