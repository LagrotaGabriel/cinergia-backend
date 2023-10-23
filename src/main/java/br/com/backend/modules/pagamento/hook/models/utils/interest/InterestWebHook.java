package br.com.backend.modules.pagamento.hook.models.utils.interest;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterestWebHook {
    private Double value;
    private String type;
}
