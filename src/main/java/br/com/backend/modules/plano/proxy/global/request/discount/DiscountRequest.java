package br.com.backend.modules.plano.proxy.global.request.discount;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    private Double value;
    private Integer dueDateLimitDays;
}
