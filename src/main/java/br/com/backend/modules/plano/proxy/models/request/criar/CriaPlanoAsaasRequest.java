package br.com.backend.modules.plano.proxy.models.request.criar;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.proxy.models.request.enums.CycleEnum;
import br.com.backend.modules.plano.proxy.models.request.global.discount.DiscountRequest;
import br.com.backend.modules.plano.proxy.models.request.global.fine.FineRequest;
import br.com.backend.modules.plano.proxy.models.request.global.interest.InterestRequest;
import br.com.backend.modules.plano.proxy.models.request.criar.split.SplitRequest;
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
