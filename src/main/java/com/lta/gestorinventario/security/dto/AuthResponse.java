package com.lta.gestorinventario.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private Long usuarioId;
    private String username;

    public AuthResponse(String token) {
        this.token = token;
    }

}
