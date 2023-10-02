package br.com.backend.globals.models.acesso.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PerfilEnum {

    EMPRESA (0, "Empresa", "EMPRESA"),
    CLIENTE (1, "Cliente", "CLIENTE"),
    ADMIN(2, "Admin", "ADMIN");

    private final int code;
    private final String desc;
    private final String role;

}

