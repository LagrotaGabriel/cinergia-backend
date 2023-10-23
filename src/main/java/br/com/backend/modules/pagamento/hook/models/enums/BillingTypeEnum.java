package br.com.backend.modules.pagamento.hook.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BillingTypeEnum {
    BOLETO("Boleto Bancário", "BOLETO"),
    CREDIT_CARD("Cartão de crédito", "CREDIT_CARD"),
    DEBIT_CARD("Cartão de débito", "DEBIT_CARD"), //TODO VALIDAR PARA NÃO LANÇAR NULL POINTER
    UNDEFINED("Perguntar ao cliente", null), //TODO VALIDAR PARA NÃO LANÇAR NULL POINTER
    TRANSFER("Transferência bancária", null), //TODO VALIDAR PARA NÃO LANÇAR NULL POINTER
    DEPOSIT("Depósito bancário", null), //TODO VALIDAR PARA NÃO LANÇAR NULL POINTER
    PIX("Recebimento via Pix", "PIX");
    private final String desc;
    private final String formaPagamentoResumida;
}