package br.com.backend.modules.transferencia.proxy.models.enums;

import lombok.Getter;

@Getter
public enum StatusTransferePixEnum {
    PENDING, BANK_PROCESSING, DONE, CANCELLED, FAILED
}
