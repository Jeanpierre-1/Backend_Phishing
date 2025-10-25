package com.lta.gestorinventario.servicesinterfaces;

import com.lta.gestorinventario.dtos.PhishingDetectionResponse;

public interface IPhishingDetectionService {
    /**
     * Analiza una URL usando la API de detección de phishing
     * @param url URL a analizar
     * @return Respuesta detallada del análisis
     */
    PhishingDetectionResponse analyzeUrl(String url);
    
    /**
     * Verifica si la API de ML está disponible
     * @return true si la API está activa
     */
    boolean isApiAvailable();
}
