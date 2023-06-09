package br.com.backend.proxy.transferencia.response;

import br.com.backend.proxy.transferencia.enums.StatusTransferePixEnum;
import br.com.backend.proxy.transferencia.enums.TipoTransferenciaEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferePixAsaasResponse {
    private String id;
    private String dateCreated;
    private Double value;
    private Double netValue;
    private StatusTransferePixEnum status;
    private Double transferFee;
    private String effectiveDate;
    private String scheduleDate;
    private Boolean authorized;
    private String failReason;
    private String transactionReceiptUrl;
    private TipoTransferenciaEnum operationType;
    private String description;
}
