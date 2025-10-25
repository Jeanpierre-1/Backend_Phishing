package com.lta.gestorinventario.security.dto;

import com.lta.gestorinventario.enums.RolEnum;
import lombok.Data;

import java.util.Set;

@Data
public class RegistroRequest {

    private String username;
    private String password;
    private Set<RolEnum> roles;
    private String nombre;
    private String apellido;

}
