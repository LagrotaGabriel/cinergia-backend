package br.com.backend.modules.transferencia.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusTransferenciaEnum {

    PENDENTE (0, "Pendente"),
    SUCESSO(1, "Sucesso"),
    FALHA_SALDO_INSUFICIENTE(2, "Saldo insuficiente"),
    FALHA_CHAVE_INEXISTENTE(3, "Chave pix inexistente"),
    FALHA(4, "Falha"),
    CANCELADO(5, "Cancelado");

    private final int code;
    private final String desc;

}
