package com.example.hrm.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${hrm.app.jwtSecret}")
    private String jwtSecret;

    @Value("${hrm.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Trả về SecretKey chuẩn thay vì Key chung chung như bản cũ
    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // hàm tạo token
    public String generateJwtToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true; // Hợp lệ
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token không hợp lệ: " + e.getMessage());
        }
        return false; // Không hợp lệ
    }
}