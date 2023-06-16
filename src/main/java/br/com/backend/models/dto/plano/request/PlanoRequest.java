package br.com.backend.models.dto.plano.request;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.NotificacaoEnum;
import br.com.backend.models.enums.PeriodicidadeEnum;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequest {
    private Long id;
    private Long idClienteResponsavel;
    private String dataInicio;
    private String descricao;
    private Double valor;
    private FormaPagamentoEnum formaPagamento;
    private PeriodicidadeEnum periodicidade;
    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();
}
