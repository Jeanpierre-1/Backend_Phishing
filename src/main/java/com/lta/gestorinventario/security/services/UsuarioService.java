package com.lta.gestorinventario.security.services;

import com.lta.gestorinventario.entity.Rol;
import com.lta.gestorinventario.entity.Usuario;
import com.lta.gestorinventario.security.repository.RolRepository;
import com.lta.gestorinventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario buscarPorNombre(String username){
        return usuarioRepository.findByUsername(username);
    }

    @Transactional
    public Usuario guardarUsuario(Usuario usuario){
        usuario.setUsername(usuario.getUsername());
        usuario.setRoles(usuario.getRoles());
        usuario.setPassword(usuario.getPassword());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        for (Rol rol : usuario.getRoles()) {
            rol.getUsuarios().add(usuarioGuardado);
        }
        return usuarioGuardado;
    }
}
