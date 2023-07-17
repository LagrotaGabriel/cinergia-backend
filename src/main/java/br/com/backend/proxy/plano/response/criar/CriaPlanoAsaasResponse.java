package br.com.backend.proxy.plano.response.criar;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.proxy.plano.request.CycleEnum;
import br.com.backend.proxy.plano.request.split.SplitRequest;
import br.com.backend.proxy.plano.response.global.StatusEnum;
import br.com.backend.proxy.plano.response.global.discount.DiscountResponse;
import br.com.backend.proxy.plano.response.global.fine.FineResponse;
import br.com.backend.proxy.plano.response.global.interest.InterestResponse;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriaPlanoAsaasResponse {
    private String id;
    private String dateCreated;
    private String customer;
    private String paymentLink;
    private FormaPagamentoEnum billingType;
    private Double value;
    private String nextDueDate;
    private DiscountResponse discount;
    private InterestResponse interest;
    private FineResponse fine;
    private CycleEnum cycle;
    private String description;
    private String endDate;
    private String maxPayments;
    private StatusEnum status;
    private String externalReference;
    private SplitRequest split;
}
