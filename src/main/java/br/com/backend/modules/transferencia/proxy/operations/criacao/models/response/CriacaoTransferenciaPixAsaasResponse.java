package br.com.backend.modules.transferencia.proxy.operations.criacao.models.response;

import br.com.backend.modules.transferencia.proxy.operations.criacao.models.enums.StatusTransferePixEnum;
import br.com.backend.modules.transferencia.proxy.operations.criacao.models.enums.TipoTransferenciaEnum;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CriacaoTransferenciaPixAsaasResponse {
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
