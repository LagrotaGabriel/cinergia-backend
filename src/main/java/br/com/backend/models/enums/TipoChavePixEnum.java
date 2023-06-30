package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoChavePixEnum {
    CPF(0, "Cpf"),
    CNPJ(1, "Cnpj"),
    EMAIL(2, "E-mail"),
    PHONE(3, "Telefone"),
    EVP(4, "Chave aleatória");

    private final int code;
    private final String desc;
}
