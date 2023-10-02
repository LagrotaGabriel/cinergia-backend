package br.com.backend.modules.plano.models.dto.request;

import br.com.backend.models.cartao.dto.request.CartaoRequest;
import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @NotNull
    private String dataInicio;
    @NotNull
    private String descricao;
    @NotNull
    private Double valor;
    @NotNull
    private FormaPagamentoEnum formaPagamento;
    @NotNull
    private PeriodicidadeEnum periodicidade;
    private Set<NotificacaoEnum> notificacoes = new HashSet<>();
    private CartaoRequest cartao;
}
