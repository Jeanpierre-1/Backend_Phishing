package com.lta.gestorinventario.dtos;

import com.lta.gestorinventario.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private Set<String> roles;
    private Integer cantidadEnlaces;
}
