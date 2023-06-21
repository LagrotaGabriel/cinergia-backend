package br.com.backend.models.dto.pagamento.request;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPagamentoEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoRequest {
    private Long id;
    private Double valorBruto;
    private String descricao;
    private FormaPagamentoEnum formaPagamento;
    private StatusPagamentoEnum statusPagamento;
}
