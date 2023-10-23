package br.com.backend.modules.pagamento.hook.models.utils.fine;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FineWebHook {
    private Double value;
    private String type;
}
