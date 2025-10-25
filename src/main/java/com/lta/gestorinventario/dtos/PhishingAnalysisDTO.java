package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhishingAnalysisDTO {
    private Long enlaceId;
    private String url;
    private Boolean isPhishing;
    private Double probabilityPhishing;
    private String confidence;
    private String message;
    private String recommendation;
    private String timestamp;
    
    // Información resumida del análisis
    private Integer urlLength;
    private String domain;
    private Boolean hasHttps;
    private Integer suspiciousKeywordsCount;
    private Integer specialCharactersCount;
}
