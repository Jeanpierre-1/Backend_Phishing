package com.lta.gestorinventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {
    private Long urlsAnalizadasEstaSemana;
    private Double porcentajeCambioSemana;
    private Long totalAnalisis;
    private Double porcentajeCambioMes;
    private Long usuariosRegistrados;
    private DistribucionGlobalDTO distribucionGlobal;
    private java.util.List<AplicacionPhishingDTO> topAplicacionesPhishing;
}
