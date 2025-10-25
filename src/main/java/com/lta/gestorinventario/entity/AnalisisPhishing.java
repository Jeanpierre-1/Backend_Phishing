package com.lta.gestorinventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "analisis_phishing")
@Data
public class AnalisisPhishing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enlace_id", nullable = false)
    private Enlace enlace;
    
    // Resultados del análisis
    @Column(name = "is_phishing")
    private Boolean isPhishing;
    
    @Column(name = "probability_phishing")
    private Double probabilityPhishing;
    
    @Column(name = "confidence")
    private String confidence; // HIGH, MEDIUM, LOW
    
    @Column(name = "label")
    private Integer label; // 0 = Phishing, 1 = Legítima
    
    @Column(name = "ml_message", length = 500)
    private String mlMessage;
    
    @Column(name = "recommendation", length = 1000)
    private String recommendation;
    
    // Análisis de URL
    @Column(name = "url_length")
    private Integer urlLength;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "domain_length")
    private Integer domainLength;
    
    @Column(name = "path_length")
    private Integer pathLength;
    
    @Column(name = "protocol")
    private String protocol;
    
    @Column(name = "has_https")
    private Boolean hasHttps;
    
    @Column(name = "has_query")
    private Boolean hasQuery;
    
    // Indicadores de riesgo
    @Column(name = "special_characters_count")
    private Integer specialCharactersCount;
    
    @Column(name = "digits_in_url")
    private Integer digitsInUrl;
    
    @Column(name = "digits_in_domain")
    private Integer digitsInDomain;
    
    @Column(name = "has_repeated_digits")
    private Boolean hasRepeatedDigits;
    
    @Column(name = "number_of_subdomains")
    private Integer numberOfSubdomains;
    
    @Column(name = "dots_in_domain")
    private Integer dotsInDomain;
    
    @Column(name = "hyphens_in_domain")
    private Integer hyphensInDomain;
    
    @Column(name = "suspicious_keywords_count")
    private Integer suspiciousKeywordsCount;
    
    @Column(name = "suspicious_keywords", length = 1000)
    private String suspiciousKeywords; // JSON o CSV con las palabras encontradas
    
    // Metadatos
    @Column(name = "analysis_timestamp", nullable = false)
    private LocalDateTime analysisTimestamp;
    
    @Column(name = "api_response_time_ms")
    private Long apiResponseTimeMs;
    
    @Column(name = "analysis_version")
    private String analysisVersion; // Versión del modelo ML
    
    @PrePersist
    protected void onCreate() {
        if (analysisTimestamp == null) {
            analysisTimestamp = LocalDateTime.now();
        }
        if (analysisVersion == null) {
            analysisVersion = "1.0";
        }
    }
}
