package com.lta.gestorinventario.servicesimplements;

import com.lta.gestorinventario.dtos.PhishingDetectionResponse;
import com.lta.gestorinventario.servicesinterfaces.IPhishingDetectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PhishingDetectionServiceImpl implements IPhishingDetectionService {

    @Value("${phishing.api.url:http://localhost:8000}")
    private String apiBaseUrl;

    private final RestTemplate restTemplate;

    public PhishingDetectionServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public PhishingDetectionResponse analyzeUrl(String url) {
        try {
            String endpoint = apiBaseUrl + "/predict-url-detailed";
            
            // Crear el request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("url", url);
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            // Realizar la petición
            ResponseEntity<PhishingDetectionResponse> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                request,
                PhishingDetectionResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al obtener respuesta de la API de ML");
            }
            
        } catch (RestClientException e) {
            throw new RuntimeException("No se pudo conectar con la API de detección de phishing: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isApiAvailable() {
        try {
            String endpoint = apiBaseUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(endpoint, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}
