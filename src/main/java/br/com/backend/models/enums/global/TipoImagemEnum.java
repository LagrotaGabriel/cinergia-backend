package br.com.backend.models.enums.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoImagemEnum {

    JPG(0, "jpeg"),
    PNG(1, "png");

    private final int code;
    private final String desc;

}