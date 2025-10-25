package com.lta.gestorinventario.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PhishingDetectionResponse {
    private String url;
    private String timestamp;
    
    @JsonProperty("is_phishing")
    private Boolean isPhishing;
    
    private Double probability;
    private String confidence;
    private Integer label;
    private String message;
    private String recommendation;
    
    @JsonProperty("url_analysis")
    private UrlAnalysis urlAnalysis;
    
    @JsonProperty("risk_indicators")
    private RiskIndicators riskIndicators;
    
    @Data
    public static class UrlAnalysis {
        @JsonProperty("url_length")
        private Integer urlLength;
        
        private String domain;
        
        @JsonProperty("domain_length")
        private Integer domainLength;
        
        private String path;
        
        @JsonProperty("path_length")
        private Integer pathLength;
        
        private String protocol;
        
        @JsonProperty("has_https")
        private Boolean hasHttps;
        
        @JsonProperty("query_parameters")
        private String queryParameters;
        
        @JsonProperty("has_query")
        private Boolean hasQuery;
        
        private String fragment;
    }
    
    @Data
    public static class RiskIndicators {
        @JsonProperty("special_characters_count")
        private Integer specialCharactersCount;
        
        @JsonProperty("digits_in_url")
        private Integer digitsInUrl;
        
        @JsonProperty("digits_in_domain")
        private Integer digitsInDomain;
        
        @JsonProperty("has_repeated_digits")
        private Boolean hasRepeatedDigits;
        
        @JsonProperty("number_of_subdomains")
        private Integer numberOfSubdomains;
        
        @JsonProperty("dots_in_domain")
        private Integer dotsInDomain;
        
        @JsonProperty("dots_in_url")
        private Integer dotsInUrl;
        
        @JsonProperty("hyphens_in_domain")
        private Integer hyphensInDomain;
        
        @JsonProperty("slashes_in_url")
        private Integer slashesInUrl;
        
        @JsonProperty("suspicious_keywords_found")
        private List<String> suspiciousKeywordsFound;
        
        @JsonProperty("suspicious_keywords_count")
        private Integer suspiciousKeywordsCount;
    }
}
