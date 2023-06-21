package br.com.backend.models.dto.plano.response;

import br.com.backend.models.enums.NotificacaoEnum;
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
    private String dataVencimento;
    private String dataInicio;
    private String descricao;
    private Double valor;
    private String formaPagamento;
    private String statusPlano;
    private String periodicidade;

    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();
}
