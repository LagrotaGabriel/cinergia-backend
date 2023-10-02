package br.com.backend.globals.models.telefone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoTelefoneEnum {

    FIXO (0, "Fixo"),
    MOVEL (1, "Móvel"),
    MOVEL_WHATSAPP (2, "Móvel com whatsapp");

    private final int code;
    private final String desc;
}

