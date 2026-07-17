package com.example.hrm.controller;

import com.example.hrm.dto.response.JwtResponse;
import com.example.hrm.dto.request.LoginRequest;
import com.example.hrm.security.CustomUserDetails;
import com.example.hrm.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Tự động inject các phụ thuộc có final
public class AuthController {

    // Trưởng phòng an ninh (Dùng để kiểm tra mật khẩu)
    private final AuthenticationManager authenticationManager;

    // Máy in thẻ (Dùng để in token)
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try{
            // Bước 1: Giao username và password cho Trưởng phòng an ninh kiểm tra
            // Nếu sai mật khẩu, hệ thống sẽ tự động văng lỗi (Exception) và dừng lại ở đây.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Bước 2: Đăng nhập thành công! Lưu thông tin vào Giấy thông hành tạm thời của Request này
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Bước 3: Đưa Giấy thông hành vào Máy in thẻ để sinh ra Token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Bước 4: Lấy lại thông tin chi tiết của User (Tên, Quyền) để nhét vào phong bì trả về
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Dùng Stream API của Java bóc tách danh sách quyền thành List<String> (VD: ["ROLE_ADMIN"])
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Bước 5: Đóng gói tất cả vào JwtResponse và trả về cho Client với mã HTTP 200 (OK)
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
        }catch (DisabledException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Đăng nhập thất bại: Tài khoản của bạn đã bị khóa!");
        }catch (BadCredentialsException e) {
            // NẾU SAI MẬT KHẨU / SAI TÀI KHOẢN -> Nó sẽ nhảy vào đây
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Đăng nhập thất bại: Sai tên đăng nhập hoặc mật khẩu!");
        }

    }
}