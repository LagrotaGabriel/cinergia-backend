package br.com.backend.models.dto.plano.response;

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
