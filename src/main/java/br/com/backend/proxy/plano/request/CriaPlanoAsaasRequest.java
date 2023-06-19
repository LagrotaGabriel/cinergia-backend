package br.com.backend.proxy.plano.request;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.proxy.plano.request.discount.DiscountRequest;
import br.com.backend.proxy.plano.request.fine.FineRequest;
import br.com.backend.proxy.plano.request.interest.InterestRequest;
import br.com.backend.proxy.plano.request.split.SplitRequest;
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
