package br.com.backend.models.dto.notificacao;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoResponse {
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private String uri;
    private Boolean lida;
    private String tipoNotificacaoEnum;
}
