package com.lta.gestorinventario.controller;


import com.lta.gestorinventario.dtos.EnlaceDTO;
import com.lta.gestorinventario.servicesinterfaces.IEnlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enlaces")
@CrossOrigin("*")
public class EnlaceController {

    @Autowired
    private IEnlaceService enlaceService;

    @PostMapping
    public ResponseEntity<EnlaceDTO> insertar(@RequestBody EnlaceDTO enlaceDTO) {
        try {
            EnlaceDTO nuevoEnlace = enlaceService.insertar(enlaceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEnlace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EnlaceDTO>> listar() {
        List<EnlaceDTO> enlaces = enlaceService.listar();
        return ResponseEntity.ok(enlaces);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnlaceDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<EnlaceDTO> enlaces = enlaceService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(enlaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnlaceDTO> obtener(@PathVariable Long id) {
        try {
            EnlaceDTO enlace = enlaceService.obtenerPorId(id);
            return ResponseEntity.ok(enlace);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnlaceDTO> actualizar(@PathVariable Long id, @RequestBody EnlaceDTO enlaceDTO) {
        try {
            EnlaceDTO enlaceActualizado = enlaceService.actualizar(id, enlaceDTO);
            return ResponseEntity.ok(enlaceActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            enlaceService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}