package com.lta.gestorinventario.controller;

import com.lta.gestorinventario.dtos.AnalisisPhishingDTO;
import com.lta.gestorinventario.entity.AnalisisPhishing;
import com.lta.gestorinventario.repositories.IAnalisisPhishingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analisis")
@CrossOrigin("*")
public class AnalisisPhishingController {

    @Autowired
    private IAnalisisPhishingRepository analisisRepository;

    @GetMapping
    public ResponseEntity<List<AnalisisPhishingDTO>> listarTodos() {
        List<AnalisisPhishing> analisis = analisisRepository.findAll();
        List<AnalisisPhishingDTO> dtos = analisis.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/enlace/{enlaceId}")
    public ResponseEntity<List<AnalisisPhishingDTO>> obtenerAnalisisPorEnlace(@PathVariable Long enlaceId) {
        List<AnalisisPhishing> analisis = analisisRepository.findByEnlaceId(enlaceId);
        List<AnalisisPhishingDTO> dtos = analisis.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/enlace/{enlaceId}/ultimo")
    public ResponseEntity<AnalisisPhishingDTO> obtenerUltimoAnalisis(@PathVariable Long enlaceId) {
        return analisisRepository.findFirstByEnlaceIdOrderByAnalysisTimestampDesc(enlaceId)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phishing")
    public ResponseEntity<List<AnalisisPhishingDTO>> obtenerAnalisisPhishing() {
        List<AnalisisPhishing> analisis = analisisRepository.findByIsPhishingTrue();
        List<AnalisisPhishingDTO> dtos = analisis.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalisisPhishingDTO> obtenerPorId(@PathVariable Long id) {
        return analisisRepository.findById(id)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAnalisis(@PathVariable Long id) {
        if (!analisisRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        analisisRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AnalisisPhishingDTO convertirADTO(AnalisisPhishing analisis) {
        return new AnalisisPhishingDTO(
                analisis.getId(),
                analisis.getEnlace().getId(),
                analisis.getIsPhishing(),
                analisis.getProbabilityPhishing(),
                analisis.getConfidence(),
                analisis.getLabel(),
                analisis.getMlMessage(),
                analisis.getRecommendation(),
                analisis.getUrlLength(),
                analisis.getDomain(),
                analisis.getDomainLength(),
                analisis.getPathLength(),
                analisis.getProtocol(),
                analisis.getHasHttps(),
                analisis.getHasQuery(),
                analisis.getSpecialCharactersCount(),
                analisis.getDigitsInUrl(),
                analisis.getDigitsInDomain(),
                analisis.getHasRepeatedDigits(),
                analisis.getNumberOfSubdomains(),
                analisis.getDotsInDomain(),
                analisis.getHyphensInDomain(),
                analisis.getSuspiciousKeywordsCount(),
                analisis.getSuspiciousKeywords(),
                analisis.getAnalysisTimestamp(),
                analisis.getApiResponseTimeMs(),
                analisis.getAnalysisVersion()
        );
    }
}
