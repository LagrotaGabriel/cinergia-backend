package br.com.backend.proxy.plano.response.split;

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
