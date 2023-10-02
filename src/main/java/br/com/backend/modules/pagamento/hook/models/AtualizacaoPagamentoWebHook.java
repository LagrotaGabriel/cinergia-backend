package br.com.backend.modules.pagamento.hook.models;

import br.com.backend.modules.pagamento.hook.models.enums.EventoCobrancaEnum;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoPagamentoWebHook {
    private EventoCobrancaEnum event;
    private PagamentoWebHook payment;
}
