package br.com.backend.modules.plano.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificacaoEnum {

    WHATSAPP (0, "Whatsapp"),
    EMAIL (1, "E-mail"),
    SISTEMA (3, "Sistema");

    private final int code;
    private final String desc;

}
