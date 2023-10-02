package br.com.backend.modules.plano.proxy.models.response.atualizar;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaDiscountResponse {
    private Double value;
    private Integer dueDateLimitDays;
}
