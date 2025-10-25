package com.lta.gestorinventario.servicesinterfaces;

import com.lta.gestorinventario.dtos.EnlaceDTO;

import java.util.List;

public interface IEnlaceService {
    EnlaceDTO insertar(EnlaceDTO enlaceDTO);
    List<EnlaceDTO> listar();
    List<EnlaceDTO> listarPorUsuario(Long usuarioId);
    EnlaceDTO obtenerPorId(Long id);
    EnlaceDTO actualizar(Long id, EnlaceDTO enlaceDTO);
    void eliminar(Long id);
}
