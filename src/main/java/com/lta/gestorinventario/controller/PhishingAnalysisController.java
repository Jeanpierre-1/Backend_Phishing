package com.lta.gestorinventario.controller;

import com.lta.gestorinventario.dtos.PhishingAnalysisDTO;
import com.lta.gestorinventario.dtos.PhishingDetectionResponse;
import com.lta.gestorinventario.servicesinterfaces.IPhishingDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/phishing")
@CrossOrigin("*")
public class PhishingAnalysisController {

    @Autowired
    private IPhishingDetectionService phishingDetectionService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeUrl(@RequestBody Map<String, String> request) {
        try {
            String url = request.get("url");
            
            if (url == null || url.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La URL es requerida");
            }
            
            PhishingDetectionResponse response = phishingDetectionService.analyzeUrl(url);
            
            // Crear un DTO simplificado para la respuesta
            PhishingAnalysisDTO analysisDTO = new PhishingAnalysisDTO();
            analysisDTO.setUrl(response.getUrl());
            analysisDTO.setIsPhishing(response.getIsPhishing());
            analysisDTO.setProbabilityPhishing(response.getProbability());
            analysisDTO.setConfidence(response.getConfidence());
            analysisDTO.setMessage(response.getMessage());
            analysisDTO.setRecommendation(response.getRecommendation());
            analysisDTO.setTimestamp(response.getTimestamp());
            
            if (response.getUrlAnalysis() != null) {
                analysisDTO.setUrlLength(response.getUrlAnalysis().getUrlLength());
                analysisDTO.setDomain(response.getUrlAnalysis().getDomain());
                analysisDTO.setHasHttps(response.getUrlAnalysis().getHasHttps());
            }
            
            if (response.getRiskIndicators() != null) {
                analysisDTO.setSuspiciousKeywordsCount(response.getRiskIndicators().getSuspiciousKeywordsCount());
                analysisDTO.setSpecialCharactersCount(response.getRiskIndicators().getSpecialCharactersCount());
            }
            
            return ResponseEntity.ok(analysisDTO);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se pudo analizar la URL");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    @PostMapping("/analyze-detailed")
    public ResponseEntity<?> analyzeUrlDetailed(@RequestBody Map<String, String> request) {
        try {
            String url = request.get("url");
            
            if (url == null || url.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La URL es requerida");
            }
            
            PhishingDetectionResponse response = phishingDetectionService.analyzeUrl(url);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se pudo analizar la URL");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> checkApiHealth() {
        boolean isAvailable = phishingDetectionService.isApiAvailable();
        
        Map<String, Object> status = new HashMap<>();
        status.put("ml_api_available", isAvailable);
        status.put("status", isAvailable ? "OK" : "UNAVAILABLE");
        
        return ResponseEntity.ok(status);
    }
}
