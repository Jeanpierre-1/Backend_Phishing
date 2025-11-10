package com.lta.gestorinventario.controller;

import com.lta.gestorinventario.dtos.AplicacionPhishingDTO;
import com.lta.gestorinventario.dtos.DistribucionGlobalDTO;
import com.lta.gestorinventario.dtos.EstadisticasDTO;
import com.lta.gestorinventario.repositories.IAnalisisPhishingRepository;
import com.lta.gestorinventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin("*")
public class EstadisticasController {

    @Autowired
    private IAnalisisPhishingRepository analisisRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
        EstadisticasDTO estadisticas = new EstadisticasDTO();

        // 1. URLs analizadas esta semana
        LocalDateTime inicioSemana = obtenerInicioSemana();
        LocalDateTime inicioSemanaAnterior = inicioSemana.minus(7, ChronoUnit.DAYS);
        
        long urlsEstaSemana = analisisRepository.contarAnalisisEstaSemana(inicioSemana);
        long urlsSemanaAnterior = analisisRepository.contarAnalisisEstaSemana(inicioSemanaAnterior) - urlsEstaSemana;
        
        estadisticas.setUrlsAnalizadasEstaSemana(urlsEstaSemana);
        
        // Calcular porcentaje de cambio
        if (urlsSemanaAnterior > 0) {
            double porcentajeCambio = ((double) (urlsEstaSemana - urlsSemanaAnterior) / urlsSemanaAnterior) * 100;
            estadisticas.setPorcentajeCambioSemana(Math.round(porcentajeCambio * 100.0) / 100.0);
        } else {
            estadisticas.setPorcentajeCambioSemana(100.0);
        }

        // 2. Total de análisis
        long totalAnalisis = analisisRepository.contarTotalAnalisis();
        estadisticas.setTotalAnalisis(totalAnalisis);
        
        // Calcular porcentaje de cambio del mes
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long analisisMesActual = analisisRepository.contarAnalisisEstaSemana(inicioMes);
        estadisticas.setPorcentajeCambioMes(100.0); // Puedes ajustar esto según necesites

        // 3. Total de usuarios registrados
        long totalUsuarios = usuarioRepository.count();
        estadisticas.setUsuariosRegistrados(totalUsuarios);

        // 4. Distribución global
        long totalPhishing = analisisRepository.contarPhishing();
        long totalLegitimas = analisisRepository.contarLegitimos();
        long total = totalPhishing + totalLegitimas;

        DistribucionGlobalDTO distribucion = new DistribucionGlobalDTO();
        distribucion.setTotalPhishing(totalPhishing);
        distribucion.setTotalLegitimas(totalLegitimas);
        
        if (total > 0) {
            double porcentajePhishing = ((double) totalPhishing / total) * 100;
            double porcentajeLegitimas = ((double) totalLegitimas / total) * 100;
            distribucion.setPorcentajePhishing(Math.round(porcentajePhishing * 100.0) / 100.0);
            distribucion.setPorcentajeLegitimas(Math.round(porcentajeLegitimas * 100.0) / 100.0);
        } else {
            distribucion.setPorcentajePhishing(0.0);
            distribucion.setPorcentajeLegitimas(0.0);
        }
        
        estadisticas.setDistribucionGlobal(distribucion);

        // 5. Top aplicaciones con phishing
        List<Object[]> phishingPorApp = analisisRepository.contarPhishingPorAplicacion();
        List<AplicacionPhishingDTO> topAplicaciones = new ArrayList<>();
        
        for (Object[] row : phishingPorApp) {
            String aplicacion = (String) row[0];
            Long cantidad = (Long) row[1];
            
            double porcentaje = 0.0;
            if (totalPhishing > 0) {
                porcentaje = ((double) cantidad / totalPhishing) * 100;
                porcentaje = Math.round(porcentaje * 100.0) / 100.0;
            }
            
            topAplicaciones.add(new AplicacionPhishingDTO(aplicacion, cantidad, porcentaje));
        }
        
        estadisticas.setTopAplicacionesPhishing(topAplicaciones);

        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/distribucion")
    public ResponseEntity<DistribucionGlobalDTO> obtenerDistribucion() {
        long totalPhishing = analisisRepository.contarPhishing();
        long totalLegitimas = analisisRepository.contarLegitimos();
        long total = totalPhishing + totalLegitimas;

        DistribucionGlobalDTO distribucion = new DistribucionGlobalDTO();
        distribucion.setTotalPhishing(totalPhishing);
        distribucion.setTotalLegitimas(totalLegitimas);
        
        if (total > 0) {
            double porcentajePhishing = ((double) totalPhishing / total) * 100;
            double porcentajeLegitimas = ((double) totalLegitimas / total) * 100;
            distribucion.setPorcentajePhishing(Math.round(porcentajePhishing * 100.0) / 100.0);
            distribucion.setPorcentajeLegitimas(Math.round(porcentajeLegitimas * 100.0) / 100.0);
        } else {
            distribucion.setPorcentajePhishing(0.0);
            distribucion.setPorcentajeLegitimas(0.0);
        }

        return ResponseEntity.ok(distribucion);
    }

    @GetMapping("/aplicaciones-phishing")
    public ResponseEntity<List<AplicacionPhishingDTO>> obtenerTopAplicaciones() {
        List<Object[]> phishingPorApp = analisisRepository.contarPhishingPorAplicacion();
        List<AplicacionPhishingDTO> topAplicaciones = new ArrayList<>();
        
        long totalPhishing = analisisRepository.contarPhishing();
        
        for (Object[] row : phishingPorApp) {
            String aplicacion = (String) row[0];
            Long cantidad = (Long) row[1];
            
            double porcentaje = 0.0;
            if (totalPhishing > 0) {
                porcentaje = ((double) cantidad / totalPhishing) * 100;
                porcentaje = Math.round(porcentaje * 100.0) / 100.0;
            }
            
            topAplicaciones.add(new AplicacionPhishingDTO(aplicacion, cantidad, porcentaje));
        }

        return ResponseEntity.ok(topAplicaciones);
    }

    @GetMapping("/urls-semana")
    public ResponseEntity<Long> obtenerUrlsEstaSemana() {
        LocalDateTime inicioSemana = obtenerInicioSemana();
        long urlsEstaSemana = analisisRepository.contarAnalisisEstaSemana(inicioSemana);
        return ResponseEntity.ok(urlsEstaSemana);
    }

    @GetMapping("/total-analisis")
    public ResponseEntity<Long> obtenerTotalAnalisis() {
        long total = analisisRepository.contarTotalAnalisis();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total-usuarios")
    public ResponseEntity<Long> obtenerTotalUsuarios() {
        long total = usuarioRepository.count();
        return ResponseEntity.ok(total);
    }

    /**
     * Obtiene el inicio de la semana actual (lunes a las 00:00:00)
     */
    private LocalDateTime obtenerInicioSemana() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSemana = ahora.with(DayOfWeek.MONDAY)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        
        // Si hoy es antes del lunes, retroceder a la semana anterior
        if (ahora.isBefore(inicioSemana)) {
            inicioSemana = inicioSemana.minus(7, ChronoUnit.DAYS);
        }
        
        return inicioSemana;
    }
}
