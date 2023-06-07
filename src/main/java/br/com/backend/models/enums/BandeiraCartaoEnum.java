package br.com.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BandeiraCartaoEnum {

    MASTERCARD (0, "Mastercard"),
    VISA (1, "Visa"),
    AMERICAN_EXPRESS (2, "American Express"),
    HIPERCARD (3, "Hipercard");

    private final int code;
    private final String desc;
}
