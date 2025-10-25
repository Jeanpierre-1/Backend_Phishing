package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisPhishingDTO {
    private Long id;
    private Long enlaceId;
    
    // Resultados principales
    private Boolean isPhishing;
    private Double probabilityPhishing;
    private String confidence;
    private Integer label;
    private String mlMessage;
    private String recommendation;
    
    // Análisis de URL
    private Integer urlLength;
    private String domain;
    private Integer domainLength;
    private Integer pathLength;
    private String protocol;
    private Boolean hasHttps;
    private Boolean hasQuery;
    
    // Indicadores de riesgo
    private Integer specialCharactersCount;
    private Integer digitsInUrl;
    private Integer digitsInDomain;
    private Boolean hasRepeatedDigits;
    private Integer numberOfSubdomains;
    private Integer dotsInDomain;
    private Integer hyphensInDomain;
    private Integer suspiciousKeywordsCount;
    private String suspiciousKeywords;
    
    // Metadatos
    private LocalDateTime analysisTimestamp;
    private Long apiResponseTimeMs;
    private String analysisVersion;
}
