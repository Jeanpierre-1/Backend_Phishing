package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    private String username;
    private String nombre;
    private String apellido;
    private String password; // Opcional, solo si se quiere cambiar
    private Set<String> roles;
}
