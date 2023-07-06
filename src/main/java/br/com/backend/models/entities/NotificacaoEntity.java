package br.com.backend.models.entities;

import br.com.backend.models.enums.TipoNotificacaoEnum;
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
    private TipoNotificacaoEnum tipoNotificacaoEnum;
}
