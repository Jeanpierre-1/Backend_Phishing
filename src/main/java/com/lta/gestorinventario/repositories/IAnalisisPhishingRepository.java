package com.lta.gestorinventario.repositories;

import com.lta.gestorinventario.entity.AnalisisPhishing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAnalisisPhishingRepository extends JpaRepository<AnalisisPhishing, Long> {
    
    /**
     * Obtiene todos los análisis de un enlace específico
     */
    List<AnalisisPhishing> findByEnlaceId(Long enlaceId);
    
    /**
     * Obtiene el análisis más reciente de un enlace
     */
    Optional<AnalisisPhishing> findFirstByEnlaceIdOrderByAnalysisTimestampDesc(Long enlaceId);
    
    /**
     * Obtiene análisis que detectaron phishing
     */
    List<AnalisisPhishing> findByIsPhishingTrue();
    
    /**
     * Cuenta cuántos análisis de phishing tiene un enlace
     */
    long countByEnlaceIdAndIsPhishingTrue(Long enlaceId);
    
    /**
     * Obtiene todos los análisis de enlaces que pertenecen a un usuario específico
     */
    @Query("SELECT a FROM AnalisisPhishing a WHERE a.enlace.usuario.id = :usuarioId")
    List<AnalisisPhishing> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
