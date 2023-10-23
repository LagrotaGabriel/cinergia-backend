package br.com.backend.modules.notificacao.models.dto;

import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import lombok.*;

import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResponse {
    private UUID uuid;
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private String uri;
    private Boolean lida;
    private String tipoNotificacao;

    public NotificacaoResponse converteNotificacaoEntityEmNotificacaoResponse(NotificacaoEntity notificacaoEntity) {
        return NotificacaoResponse.builder()
                .uuid(notificacaoEntity.getUuid())
                .dataCadastro(notificacaoEntity.getDataCadastro())
                .horaCadastro(notificacaoEntity.getHoraCadastro())
                .descricao(notificacaoEntity.getDescricao())
                .uri(notificacaoEntity.getUri())
                .lida(notificacaoEntity.getLida())
                .tipoNotificacao(notificacaoEntity.getTipoNotificacaoEnum().getDesc())
                .build();
    }
}
