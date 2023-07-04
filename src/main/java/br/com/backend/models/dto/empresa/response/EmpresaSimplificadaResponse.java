package br.com.backend.models.dto.empresa.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaSimplificadaResponse {
    private String nomeEmpresa;
    private Double saldo;
}
