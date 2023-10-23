package br.com.backend.modules.pagamento.hook.models.utils.discount;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountWebHook {

    private Double value;
    private Integer dueDateLimitDays;
    private String type;
}
