package br.com.backend.proxy.transferencia.enums;

import lombok.Getter;

@Getter
public enum StatusChavePixAsaas {
    AWAITING_ACTIVATION, ACTIVE, AWAITING_DELETION, AWAITING_ACCOUNT_DELETION, DELETED, ERROR
}
