package br.com.backend.models.dto.plano.response;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {
    private Long id;
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private Double valor;
    private String dataVencimento;
    private FormaPagamentoEnum formaPagamento;
    private StatusPlanoEnum statusPlano;
}
