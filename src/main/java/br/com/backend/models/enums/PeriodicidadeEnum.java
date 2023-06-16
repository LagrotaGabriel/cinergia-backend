package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodicidadeEnum {

    DIARIO (0, "Diário"),
    SEMANAL (1, "Semanal"),
    MENSAL (2, "Mensal"),
    TRIMESTRAL (3, "Trimestral"),
    SEMESTRAL (4, "Semestral"),
    ANUAL (5, "Anual");

    private final int code;
    private final String desc;

}
