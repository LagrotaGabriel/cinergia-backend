package br.com.backend.modules.plano.proxy.models.request.criar.split;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SplitRequest {
    private String walletId;
    private Double fixedValue;
    private Double percentualValue;
}
