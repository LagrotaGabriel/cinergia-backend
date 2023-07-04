package br.com.backend.proxy.transferencia.enums;

import lombok.Getter;

@Getter
public enum StatusTransferePixEnum {
    PENDING, BANK_PROCESSING, DONE, CANCELLED, FAILED
}
