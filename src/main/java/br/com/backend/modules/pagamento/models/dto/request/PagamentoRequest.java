package br.com.backend.modules.pagamento.models.dto.request;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
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
