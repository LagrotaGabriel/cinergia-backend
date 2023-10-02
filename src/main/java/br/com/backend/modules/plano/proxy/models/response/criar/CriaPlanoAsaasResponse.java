package br.com.backend.modules.plano.proxy.models.response.criar;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.proxy.models.request.enums.CycleEnum;
import br.com.backend.modules.plano.proxy.models.response.criar.enums.StatusEnum;
import br.com.backend.modules.plano.proxy.models.response.global.discount.DiscountResponse;
import br.com.backend.modules.plano.proxy.models.response.global.fine.FineResponse;
import br.com.backend.modules.plano.proxy.models.response.global.interest.InterestResponse;
import br.com.backend.modules.plano.proxy.models.response.global.split.SplitResponse;
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
    private SplitResponse split;
}
