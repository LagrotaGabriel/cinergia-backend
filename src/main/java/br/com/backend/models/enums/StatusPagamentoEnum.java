package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPagamentoEnum {
    APROVADO(0, "Aprovado"),
    REPROVADO(1, "Reprovado"),
    PENDENTE(2, "Pendente"),
    ATRASADO(3, "Atrasado"),
    CANCELADO(4, "Cancelado");

    private final int code;
    private final String desc;
}
