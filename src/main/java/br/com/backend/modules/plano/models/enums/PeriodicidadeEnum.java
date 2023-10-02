package br.com.backend.modules.plano.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodicidadeEnum {

    SEMANAL (0, "Semanal"),
    MENSAL (1, "Mensal"),
    SEMESTRAL (2, "Semestral"),
    ANUAL (3, "Anual");

    private final int code;
    private final String desc;

}
