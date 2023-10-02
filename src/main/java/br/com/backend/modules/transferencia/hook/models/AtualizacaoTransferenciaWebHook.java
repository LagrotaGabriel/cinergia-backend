package br.com.backend.modules.transferencia.hook.models;

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
