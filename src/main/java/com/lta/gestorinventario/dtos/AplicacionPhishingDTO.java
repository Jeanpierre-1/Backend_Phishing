package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AplicacionPhishingDTO {
    private String aplicacion;
    private Long cantidad;
    private Double porcentaje;
}
