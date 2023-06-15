package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormaPagamentoEnum {
    BOLETO (0, "Boleto"),
    CARTAO_CREDITO (1, "Cartão de crédito"),
    CARTAO_DEBITO (2, "Cartão de débito"),
    PIX (3, "PIX"),
    DEFINIR(4, "Definir");

    private final int code;
    private final String desc;
}
