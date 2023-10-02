package br.com.backend.modules.plano.proxy.models.response.consultar;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSplitResponse {
    private String walletId;
    private Double fixedValue;
    private Integer percentualValue;
    private String status;
    private String refusalReason;

}
