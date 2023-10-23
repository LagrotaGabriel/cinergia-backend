package br.com.backend.modules.plano.proxy.global.response.discount;

import br.com.backend.modules.plano.proxy.global.response.discount.enums.TypeEnum;
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
