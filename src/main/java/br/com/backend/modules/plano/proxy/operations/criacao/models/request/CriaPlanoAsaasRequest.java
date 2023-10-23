package br.com.backend.modules.plano.proxy.operations.criacao.models.request;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.proxy.operations.criacao.models.request.split.SplitRequest;
import br.com.backend.modules.plano.proxy.global.request.enums.CycleEnum;
import br.com.backend.modules.plano.proxy.global.request.discount.DiscountRequest;
import br.com.backend.modules.plano.proxy.global.request.fine.FineRequest;
import br.com.backend.modules.plano.proxy.global.request.interest.InterestRequest;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriaPlanoAsaasRequest {
    private String customer;
    private FormaPagamentoEnum billingType;
    private Double value;
    private String nextDueDate;
    private DiscountRequest discount;
    private InterestRequest interest;
    private FineRequest fine;
    private CycleEnum cycle;
    private String description;
    private String endDate;
    private String maxPayments;
    private String externalReference;
    private SplitRequest split;
}
