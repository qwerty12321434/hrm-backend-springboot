package com.example.hrm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // Bước 1: Lấy token từ request
            String jwt = parseJwt(request);

            // Bước 2: Kiểm tra xem token có tồn tại và có hợp lệ không
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // Bước 3: Lấy username từ token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Bước 4: Lục database lấy thông tin đầy đủ của user (bao gồm cả các quyền)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Bước 5: Bỏ user vào một cái "Giấy thông hành" của Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // Chỗ này thường để mật khẩu, nhưng ta không cần vì đã xác thực bằng token
                                userDetails.getAuthorities()); // Danh sách quyền

                // Gắn thêm chi tiết về địa chỉ IP của người dùng
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Bước 6: Đưa Giấy thông hành cho hệ thống giữ (Ghi nhận là người này đã đăng nhập)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Không thể thiết lập xác thực người dùng: " + e.getMessage());
        }

        // Bước 7: Quan trọng nhất - Mở cửa cho request đi tiếp vào các tầng bên trong
        filterChain.doFilter(request, response);
    }

    // Hàm tiện ích: Bóc tách token từ header của HTTP Request
    private String parseJwt(HttpServletRequest request) {
        // Token thường được gửi ở header có tên là "Authorization"
        String headerAuth = request.getHeader("Authorization");

        // Chuẩn chung của thế giới: Token luôn bắt đầu bằng chữ "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Cắt bỏ 7 ký tự "Bearer " để lấy cái lõi token bên trong
        }

        return null;
    }
}