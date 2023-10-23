package br.com.backend.modules.pagamento.hook.models.utils.charge;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeBackWebHook {
    private String status;
    private String reason;
}
