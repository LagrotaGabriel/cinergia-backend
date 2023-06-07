package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CicloCobrancaEnum {
    SEMANAL (0, "Semanal"),
    QUINZENAL (1, "Quinzenal"),
    MENSAL (2, "Mensal"),
    SEMESTRAL (3, "Semestral"),
    ANUAL (4, "Anual");

    private final int code;
    private final String desc;
}
