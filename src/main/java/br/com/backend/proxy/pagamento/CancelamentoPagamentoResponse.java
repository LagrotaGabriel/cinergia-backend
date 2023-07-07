package br.com.backend.proxy.pagamento;

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
