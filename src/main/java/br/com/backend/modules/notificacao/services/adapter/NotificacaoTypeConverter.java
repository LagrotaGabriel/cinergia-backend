package br.com.backend.modules.notificacao.services.adapter;

import br.com.backend.modules.notificacao.models.dto.NotificacaoResponse;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificacaoTypeConverter {

    public List<NotificacaoResponse> converteNotificacaoEntityParaNotificacaoResponse(List<NotificacaoEntity> notificacaoEntities) {
        log.debug("Método responsável por converter lista de NotificacaoEntity para lista de NotificacaoResponse acessado");

        List<NotificacaoResponse> notificacaoResponses = new ArrayList<>();

        log.debug("Iniciando iteração da lista de entidades...");
        for (NotificacaoEntity notificacaoEntity : notificacaoEntities) {
            log.debug("Iniciando construção do objeto notificacaoResponse com atributos do notificacaoEntity " +
                    "iterado: {}", notificacaoEntity);
            NotificacaoResponse notificacaoResponse = NotificacaoResponse.builder()
                    .dataCadastro(notificacaoEntity.getDataCadastro())
                    .horaCadastro(notificacaoEntity.getHoraCadastro())
                    .tipoNotificacao(notificacaoEntity.getTipoNotificacaoEnum().getDesc())
                    .uri(notificacaoEntity.getUri())
                    .lida(notificacaoEntity.getLida())
                    .descricao(notificacaoEntity.getDescricao())
                    .build();

            log.debug("Adicionando objeto criado à lista de notificacoesResponse...");
            notificacaoResponses.add(notificacaoResponse);
        }

        log.debug("Conversão de tipos finalizada com sucesso. Retornando...");
        return notificacaoResponses;
    }

}
