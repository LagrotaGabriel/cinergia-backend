package br.com.backend.modules.transferencia.proxy.operations.cancelamento.response;

import br.com.backend.modules.transferencia.hook.models.bank.BankAccount;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoPixAsaasResponse {
    private String object;
    private String id;
    private String dateCreated;
    private String status;
    private String effectiveDate;
    private String endToEndIdentifier;
    private String type;
    private Double value;
    private Double netValue;
    private Double transferFee;
    private String scheduleDate;
    private Boolean authorized;
    private String failReason;
    private BankAccount bankAccount;
    private String transactionReceiptUrl;
    private String operationType;
    private String description;
}
