package br.com.backend.modules.cliente.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPessoaEnum {

    FISICA (0, "Física"),
    JURIDICA (1, "Jurídica");

    private final int code;
    private final String desc;

}
