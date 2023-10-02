package br.com.backend.modules.plano.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPlanoEnum {
    ATIVO (0, "Ativo"),
    INATIVO (1, "Inativo"),
    REMOVIDO (2, "Removido");

    private final int code;
    private final String desc;
}