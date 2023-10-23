package br.com.backend.modules.empresa.models.dto.response.views;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaSimplificadaResponse {
    private String nomeEmpresa;
    private Double saldo;
}
