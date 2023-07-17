package br.com.backend.proxy.plano.request.atualizar;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.proxy.plano.request.CycleEnum;
import br.com.backend.proxy.plano.request.global.discount.DiscountRequest;
import br.com.backend.proxy.plano.request.global.fine.FineRequest;
import br.com.backend.proxy.plano.request.global.interest.InterestRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaAsaasRequest {

    @NotNull
    private FormaPagamentoEnum billingType;

    @NotNull
    private Double value;

    @NotNull
    private String nextDueDate;

    private DiscountRequest discount;

    private InterestRequest interest;

    private FineRequest fine;

    @NotNull
    private CycleEnum cycle;

    private String description;

    private Boolean updatePendingPayments;

    private String externalReference;
}
