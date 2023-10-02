package br.com.backend.modules.plano.models.dto.response;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosPlanoResponse {
    private Double totalCobrancas;
    private Double totalPendente;
    private Double totalPago;
    private Double totalEmAtraso;
    private Double comprometimento;
}
