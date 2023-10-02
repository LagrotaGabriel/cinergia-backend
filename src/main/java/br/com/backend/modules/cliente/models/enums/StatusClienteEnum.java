package br.com.backend.modules.cliente.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusClienteEnum {

    COMUM(0, "Comum"),
    DEVEDOR(1, "Em débito"),
    VIP(2, "Vip"),
    ATENCAO(3, "Atenção");

    private final int code;
    private final String desc;

}
