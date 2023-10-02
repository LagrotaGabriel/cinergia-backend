package br.com.backend.modules.pagamento.hook.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillingTypeEnum {
    BOLETO("Boleto Bancário", "BOLETO"),
    CREDIT_CARD("Cartão de crédito", "CREDIT_CARD"),
    DEBIT_CARD("Cartão de débito", "DEBIT_CARD"),
    UNDEFINED("Perguntar ao cliente", null),
    TRANSFER("Transferência bancária", null),
    DEPOSIT("Depósito bancário", null),
    PIX("Recebimento via Pix", "PIX");
    private final String desc;
    private final String formaPagamentoResumida;
}