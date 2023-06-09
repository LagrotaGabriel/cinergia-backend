package br.com.backend.proxy.webhooks.transferencia;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoTransferenciaWebHook {
    private String event;
    private TransferWebHook transfer;
}
