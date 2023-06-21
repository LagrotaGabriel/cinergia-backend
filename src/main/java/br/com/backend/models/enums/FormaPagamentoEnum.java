package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormaPagamentoEnum {
    BOLETO (0, "Boleto"),
    CREDIT_CARD(1, "Cartão de crédito"),
    DEBIT_CARD(2, "Cartão de débito"),
    PIX (3, "Pix"),
    DEFINIR(4, "Definir");

    private final int code;
    private final String desc;
}
