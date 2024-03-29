package br.com.backend.modules.empresa.models.dto.response.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosGraficoResponse {
    private Double valorBruto;
    private Double valorTaxa;
    private Double valorLiquido;
}
