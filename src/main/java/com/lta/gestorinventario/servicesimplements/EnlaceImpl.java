package com.lta.gestorinventario.servicesimplements;

import com.lta.gestorinventario.dtos.EnlaceDTO;
import com.lta.gestorinventario.entity.Enlace;
import com.lta.gestorinventario.entity.Usuario;
import com.lta.gestorinventario.repositories.IEnlaceRepository;
import com.lta.gestorinventario.security.repository.UsuarioRepository;
import com.lta.gestorinventario.servicesinterfaces.IEnlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnlaceImpl implements IEnlaceService {

    @Autowired
    private IEnlaceRepository enlaceRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public EnlaceDTO insertar(EnlaceDTO enlaceDTO) {
        Usuario usuario = usuarioRepository.findById(enlaceDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Enlace enlace = new Enlace();
        enlace.setUrl(enlaceDTO.getUrl());
        enlace.setAplicacion(enlaceDTO.getAplicacion());
        enlace.setMensaje(enlaceDTO.getMensaje());
        enlace.setUsuario(usuario);

        Enlace enlaceGuardado = enlaceRepository.save(enlace);
        return convertirADTO(enlaceGuardado);
    }

    @Override
    public List<EnlaceDTO> listar() {
        return enlaceRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnlaceDTO> listarPorUsuario(Long usuarioId) {
        return enlaceRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnlaceDTO obtenerPorId(Long id) {
        Enlace enlace = enlaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enlace no encontrado"));
        return convertirADTO(enlace);
    }

    @Override
    @Transactional
    public EnlaceDTO actualizar(Long id, EnlaceDTO enlaceDTO) {
        Enlace enlace = enlaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enlace no encontrado"));

        enlace.setUrl(enlaceDTO.getUrl());
        enlace.setAplicacion(enlaceDTO.getAplicacion());
        enlace.setMensaje(enlaceDTO.getMensaje());

        Enlace enlaceActualizado = enlaceRepository.save(enlace);
        return convertirADTO(enlaceActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!enlaceRepository.existsById(id)) {
            throw new RuntimeException("Enlace no encontrado");
        }
        enlaceRepository.deleteById(id);
    }

    private EnlaceDTO convertirADTO(Enlace enlace) {
        return new EnlaceDTO(
                enlace.getId(),
                enlace.getUrl(),
                enlace.getAplicacion(),
                enlace.getMensaje(),
                enlace.getUsuario().getId()
        );
    }
}

