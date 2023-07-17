package br.com.backend.proxy.plano.response.global.discount;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {
    private Double value;
    private Integer dueDateLimitDays;
    private TypeEnum type;
}
