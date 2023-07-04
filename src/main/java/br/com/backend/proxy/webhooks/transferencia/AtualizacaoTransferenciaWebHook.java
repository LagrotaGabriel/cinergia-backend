package br.com.backend.proxy.webhooks.transferencia;

import br.com.backend.proxy.webhooks.cobranca.PagamentoWebHook;
import br.com.backend.proxy.webhooks.cobranca.enums.EventoCobrancaEnum;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoTransferenciaWebHook {
    private EventoCobrancaEnum event;
    private PagamentoWebHook payment;
}
