package com.lta.gestorinventario.repositories;

import com.lta.gestorinventario.entity.Enlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEnlaceRepository extends JpaRepository<Enlace,Long> {
    List<Enlace> findByUsuarioId(Long usuarioId);
}
