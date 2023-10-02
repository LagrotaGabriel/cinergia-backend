package br.com.backend.modules.plano.proxy.models.response.global.split;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SplitResponse {
    private String walletId;
    private Double fixedValue;
    private Double percentualValue;
    private StatusEnum statusEnum;
    private RefusalReasonEnum refusalReasonEnum;
}
