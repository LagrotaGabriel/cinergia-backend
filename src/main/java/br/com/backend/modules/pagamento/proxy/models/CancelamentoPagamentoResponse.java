package br.com.backend.modules.pagamento.proxy.models;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoPagamentoResponse {
    private Boolean deleted;
    private String id;
}
