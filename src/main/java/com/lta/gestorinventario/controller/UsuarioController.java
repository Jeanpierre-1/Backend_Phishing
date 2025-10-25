package com.lta.gestorinventario.controller;

import com.lta.gestorinventario.dtos.UsuarioDTO;
import com.lta.gestorinventario.dtos.UsuarioUpdateDTO;
import com.lta.gestorinventario.entity.Rol;
import com.lta.gestorinventario.entity.Usuario;
import com.lta.gestorinventario.security.repository.RolRepository;
import com.lta.gestorinventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> obtenerPorUsername(@PathVariable String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirADTO(usuario));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody UsuarioUpdateDTO updateDTO) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar si el nuevo username ya existe (solo si se está cambiando)
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(usuario.getUsername())) {
            Usuario usuarioExistente = usuarioRepository.findByUsername(updateDTO.getUsername());
            if (usuarioExistente != null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre de usuario ya está en uso"));
            }
            usuario.setUsername(updateDTO.getUsername());
        }

        // Actualizar campos básicos
        if (updateDTO.getNombre() != null) {
            usuario.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getApellido() != null) {
            usuario.setApellido(updateDTO.getApellido());
        }

        // Actualizar password solo si se proporciona
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        // Actualizar roles si se proporcionan
        if (updateDTO.getRoles() != null && !updateDTO.getRoles().isEmpty()) {
            Set<Rol> nuevosRoles = new HashSet<>();
            for (String rolNombre : updateDTO.getRoles()) {
                Rol rol = rolRepository.findByNombre(rolNombre);
                if (rol != null) {
                    nuevosRoles.add(rol);
                }
            }
            if (!nuevosRoles.isEmpty()) {
                usuario.setRoles(nuevosRoles);
            }
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(convertirADTO(usuarioActualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar el usuario porque tiene enlaces asociados"));
        }
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        Set<String> rolesNombres = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellido(),
                rolesNombres,
                usuario.getEnlaces() != null ? usuario.getEnlaces().size() : 0
        );
    }
}
