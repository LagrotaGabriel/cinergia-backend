package br.com.backend.modules.transferencia.proxy.operations.criacao.models.enums;

import lombok.Getter;

@Getter
public enum StatusTransferePixEnum {
    PENDING, BANK_PROCESSING, DONE, CANCELLED, FAILED
}
