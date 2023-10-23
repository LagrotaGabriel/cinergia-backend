package br.com.backend.modules.plano.models.dto.response;

import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {
    private UUID uuid;
    private String dataCadastro;
    private String horaCadastro;
    private String dataVencimento;
    private String dataAgendamentoRemocao;
    private String dataInicio;
    private String descricao;
    private Double valor;
    private String formaPagamento;
    private String statusPlano;
    private String periodicidade;
    protected List<String> notificacoes;

    public PlanoResponse constroiPlanoResponse(PlanoEntity planoEntity) {
        log.info("Método de conversão de objeto do tipo PlanoEntity para objeto do tipo PlanoResponse acessado");

        log.info("Iniciando construção do objeto PlanoResponse...");
        PlanoResponse planoResponse = PlanoResponse.builder()
                .uuid(planoEntity.getUuid())
                .dataCadastro(planoEntity.getDataCriacao())
                .horaCadastro(planoEntity.getHoraCriacao())
                .dataVencimento(planoEntity.getDataVencimento())
                .dataAgendamentoRemocao(planoEntity.getDataAgendamentoRemocao())
                .dataInicio(planoEntity.getDataInicio())
                .descricao(planoEntity.getDescricao())
                .valor(planoEntity.getValor())
                .formaPagamento(planoEntity.getFormaPagamento().toString())
                .statusPlano(planoEntity.getStatusPlano().toString())
                .periodicidade(planoEntity.getPeriodicidade().toString())
                .notificacoes(planoEntity.getNotificacoes() == null
                        ? new ArrayList<>()
                        : obtemListaDeNotificacoes(planoEntity.getNotificacoes()))
                .build();
        log.debug("Objeto PlanoResponse buildado com sucesso. Retornando...");
        return planoResponse;
    }

    public List<String> obtemListaDeNotificacoes(Set<NotificacaoEnum> notificacoes) {
        List<String> listaDeNotificacoes = new ArrayList<>();
        notificacoes.forEach(notificacaoEnum -> listaDeNotificacoes.add(notificacaoEnum.toString()));
        return listaDeNotificacoes;
    }
}
