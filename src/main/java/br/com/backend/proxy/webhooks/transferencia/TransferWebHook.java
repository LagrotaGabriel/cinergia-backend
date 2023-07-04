package br.com.backend.proxy.webhooks.transferencia;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferWebHook {
    private String object;
    private String id;
    private Double value;
    private Double netValue;
    private Double transferFee;
    private String dateCreated;
    private String status;
    private String effectiveDate;
    private String confirmedDate;
    private String endToEndIdentifier;
    private String transactionReceiptUrl;
    private String operationType;
    private String failReason;
    private String walletId;
    private String description;
    private Boolean canBeCancelled;
    private Boolean authorized;
    private String scheduleDate;
    private String type;
    private BankAccount bankAccount;
}
