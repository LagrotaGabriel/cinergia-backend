package br.com.backend.models.dto.plano.request;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequest {
    private Long id;
    private Long idClienteResponsavel;
    private String descricao;
    private Double valor;
    private String dataVencimento;
    private FormaPagamentoEnum formaPagamento;
    private StatusPlanoEnum statusPlano;
}
