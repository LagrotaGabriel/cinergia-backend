package br.com.backend.models.dto.empresa.response;

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
