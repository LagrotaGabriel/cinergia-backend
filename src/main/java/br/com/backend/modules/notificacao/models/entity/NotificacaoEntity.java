package br.com.backend.modules.notificacao.models.entity;

import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_notificacao")
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idEmpresaResponsavel;
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private String uri;
    private Boolean lida;

    @Enumerated(EnumType.STRING)
    private TipoNotificacaoPlanoEnum tipoNotificacaoEnum;
}
