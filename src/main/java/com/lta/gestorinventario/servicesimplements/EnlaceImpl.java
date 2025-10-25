package com.lta.gestorinventario.servicesimplements;

import com.lta.gestorinventario.dtos.AnalisisPhishingDTO;
import com.lta.gestorinventario.dtos.EnlaceDTO;
import com.lta.gestorinventario.dtos.PhishingDetectionResponse;
import com.lta.gestorinventario.entity.AnalisisPhishing;
import com.lta.gestorinventario.entity.Enlace;
import com.lta.gestorinventario.entity.Usuario;
import com.lta.gestorinventario.repositories.IAnalisisPhishingRepository;
import com.lta.gestorinventario.repositories.IEnlaceRepository;
import com.lta.gestorinventario.security.repository.UsuarioRepository;
import com.lta.gestorinventario.servicesinterfaces.IEnlaceService;
import com.lta.gestorinventario.servicesinterfaces.IPhishingDetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnlaceImpl implements IEnlaceService {

    @Autowired
    private IEnlaceRepository enlaceRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private IPhishingDetectionService phishingDetectionService;
    
    @Autowired
    private IAnalisisPhishingRepository analisisPhishingRepository;

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
        
        // Guardar primero el enlace
        Enlace enlaceGuardado = enlaceRepository.save(enlace);
        
        // Luego crear el análisis de phishing
        AnalisisPhishing analisis = null;
        try {
            long startTime = System.currentTimeMillis();
            PhishingDetectionResponse mlResponse = phishingDetectionService.analyzeUrl(enlaceDTO.getUrl());
            long responseTime = System.currentTimeMillis() - startTime;
            
            analisis = crearAnalisisDesdeRespuesta(mlResponse, enlaceGuardado, responseTime);
            analisisPhishingRepository.save(analisis);
            
        } catch (Exception e) {
            System.err.println("Error al analizar URL con ML: " + e.getMessage());
            // Crear análisis de error
            analisis = crearAnalisisError(enlaceGuardado, e.getMessage());
            analisisPhishingRepository.save(analisis);
        }

        return convertirADTO(enlaceGuardado, analisis);
    }
    
    private AnalisisPhishing crearAnalisisDesdeRespuesta(PhishingDetectionResponse response, 
                                                          Enlace enlace, long responseTime) {
        AnalisisPhishing analisis = new AnalisisPhishing();
        analisis.setEnlace(enlace);
        
        // Resultados principales
        analisis.setIsPhishing(response.getIsPhishing());
        analisis.setProbabilityPhishing(response.getProbability());
        analisis.setConfidence(response.getConfidence());
        analisis.setLabel(response.getLabel());
        analisis.setMlMessage(response.getMessage());
        analisis.setRecommendation(response.getRecommendation());
        
        // Análisis de URL
        if (response.getUrlAnalysis() != null) {
            analisis.setUrlLength(response.getUrlAnalysis().getUrlLength());
            analisis.setDomain(response.getUrlAnalysis().getDomain());
            analisis.setDomainLength(response.getUrlAnalysis().getDomainLength());
            analisis.setPathLength(response.getUrlAnalysis().getPathLength());
            analisis.setProtocol(response.getUrlAnalysis().getProtocol());
            analisis.setHasHttps(response.getUrlAnalysis().getHasHttps());
            analisis.setHasQuery(response.getUrlAnalysis().getHasQuery());
        }
        
        // Indicadores de riesgo
        if (response.getRiskIndicators() != null) {
            analisis.setSpecialCharactersCount(response.getRiskIndicators().getSpecialCharactersCount());
            analisis.setDigitsInUrl(response.getRiskIndicators().getDigitsInUrl());
            analisis.setDigitsInDomain(response.getRiskIndicators().getDigitsInDomain());
            analisis.setHasRepeatedDigits(response.getRiskIndicators().getHasRepeatedDigits());
            analisis.setNumberOfSubdomains(response.getRiskIndicators().getNumberOfSubdomains());
            analisis.setDotsInDomain(response.getRiskIndicators().getDotsInDomain());
            analisis.setHyphensInDomain(response.getRiskIndicators().getHyphensInDomain());
            analisis.setSuspiciousKeywordsCount(response.getRiskIndicators().getSuspiciousKeywordsCount());
            
            // Convertir lista de palabras clave a String
            if (response.getRiskIndicators().getSuspiciousKeywordsFound() != null) {
                analisis.setSuspiciousKeywords(String.join(", ", 
                    response.getRiskIndicators().getSuspiciousKeywordsFound()));
            }
        }
        
        // Metadatos
        analisis.setApiResponseTimeMs(responseTime);
        
        return analisis;
    }
    
    private AnalisisPhishing crearAnalisisError(Enlace enlace, String errorMessage) {
        AnalisisPhishing analisis = new AnalisisPhishing();
        analisis.setEnlace(enlace);
        analisis.setIsPhishing(null);
        analisis.setProbabilityPhishing(null);
        analisis.setConfidence("ERROR");
        analisis.setMlMessage("Error al analizar: " + errorMessage);
        analisis.setRecommendation("Análisis no disponible. Por favor, intente más tarde.");
        return analisis;
    }

    @Override
    public List<EnlaceDTO> listar() {
        return enlaceRepository.findAll()
                .stream()
                .map(this::convertirADTOSinAnalisis)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnlaceDTO> listarPorUsuario(Long usuarioId) {
        return enlaceRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTOSinAnalisis)
                .collect(Collectors.toList());
    }

    @Override
    public EnlaceDTO obtenerPorId(Long id) {
        Enlace enlace = enlaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enlace no encontrado"));
        return convertirADTOSinAnalisis(enlace);
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
        return convertirADTOSinAnalisis(enlaceActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!enlaceRepository.existsById(id)) {
            throw new RuntimeException("Enlace no encontrado");
        }
        enlaceRepository.deleteById(id);
    }

    // Convertir a DTO SIN análisis (solo datos del enlace)
    private EnlaceDTO convertirADTOSinAnalisis(Enlace enlace) {
        return new EnlaceDTO(
                enlace.getId(),
                enlace.getUrl(),
                enlace.getAplicacion(),
                enlace.getMensaje(),
                enlace.getUsuario().getId(),
                null  // No incluir análisis
        );
    }
    
    // Convertir a DTO CON análisis (usado al crear enlace)
    private EnlaceDTO convertirADTO(Enlace enlace, AnalisisPhishing analisis) {
        AnalisisPhishingDTO analisisDTO = analisis != null ? convertirAnalisisADTO(analisis) : null;
        
        return new EnlaceDTO(
                enlace.getId(),
                enlace.getUrl(),
                enlace.getAplicacion(),
                enlace.getMensaje(),
                enlace.getUsuario().getId(),
                analisisDTO
        );
    }
    
    private AnalisisPhishingDTO convertirAnalisisADTO(AnalisisPhishing analisis) {
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

