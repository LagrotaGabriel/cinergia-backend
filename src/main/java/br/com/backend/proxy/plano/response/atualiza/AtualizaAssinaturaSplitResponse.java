package br.com.backend.proxy.plano.response.atualiza;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaSplitResponse {
    private String walletId;
    private Double fixedValue;
    private Double percentualValue;
    private String status;
    private String refusalReason;
}
