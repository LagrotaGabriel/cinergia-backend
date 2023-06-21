package br.com.backend.proxy.webhooks.cobranca;

import br.com.backend.proxy.webhooks.cobranca.enums.EventoCobrancaEnum;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoCobrancaWebHook {
    private EventoCobrancaEnum event;
    private PagamentoWebHook payment;
}
