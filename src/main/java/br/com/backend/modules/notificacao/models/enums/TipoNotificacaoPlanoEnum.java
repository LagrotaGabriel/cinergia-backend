package br.com.backend.modules.notificacao.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoNotificacaoPlanoEnum {

    COBRANCA_CRIADA (0, "Cobrança criada"),
    COBRANCA_RECEBIDA (1, "Cobrança recebida"),
    COBRANCA_VENCIDA (2, "Cobrança vencida" ),
    TRANSFERENCIA_SUCESSO (3, "Transferência realizada"),
    TRANSFERENCIA_ERRO (4, "Transferência com erro");

    private final int code;
    private final String desc;
}
