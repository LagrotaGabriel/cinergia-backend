package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPlanoEnum {
    ATIVO (0, "Ativo"),
    INATIVO (1, "Inativo");

    private final int code;
    private final String desc;
}

