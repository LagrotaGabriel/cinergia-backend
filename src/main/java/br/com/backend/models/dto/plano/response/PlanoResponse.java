package br.com.backend.models.dto.plano.response;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.NotificacaoEnum;
import br.com.backend.models.enums.PeriodicidadeEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private String dataInicio;
    private String descricao;
    private Double valor;
    private FormaPagamentoEnum formaPagamento;
    private StatusPlanoEnum statusPlano;
    private PeriodicidadeEnum periodicidade;
    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();
}
