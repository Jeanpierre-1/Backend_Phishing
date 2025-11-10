package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistribucionGlobalDTO {
    private Long totalPhishing;
    private Long totalLegitimas;
    private Double porcentajePhishing;
    private Double porcentajeLegitimas;
}
