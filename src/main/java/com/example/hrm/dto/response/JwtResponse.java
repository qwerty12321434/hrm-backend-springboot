package com.example.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type="Bearer";
    private String username;
    private List<String> roles;

    public JwtResponse(String token, String username, List<String> roles){
        this.token=token;
        this.username=username;
        this.roles=roles;
    }
}
