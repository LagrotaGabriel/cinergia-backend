package br.com.backend.modules.notificacao.models.entity;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.notificacao.models.entity.id.NotificacaoId;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NotificacaoId.class)
@Table(name = "TB_SBS_NOTIFICACAO")
public class NotificacaoEntity {

    @Id
    @Comment("Chave primária da notificação - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_NOTIFICACAO_NTF", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Id
    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("Chave primária da notificação - ID da empresa ao qual a notificação faz parte")
    @JoinColumn(name = "COD_EMPRESA_NTF", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro da notificação foi realizado")
    @Column(name = "DT_DATACADASTRO_NTF", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da notificação foi realizado")
    @Column(name = "HR_HORACADASTRO_NTF", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Descrição da notificação")
    @Column(name = "STR_DESCRICAO_NTF", nullable = false, length = 70)
    private String descricao;

    @Comment("Link da notificação")
    @Column(name = "URI_LINK_NTF", length = 70)
    private String uri;

    @Comment("Status de leitura da notificação")
    @Column(name = "BOL_LIDA_NTF", nullable = false)
    private Boolean lida;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo da notificação: " +
            "0 - Cobrança criada, " +
            "1 - Cobrança recebida, " +
            "2 - Cobrança vencida, " +
            "3 - Transferência realizada, " +
            "4 - Transferência com erro")
    @Column(name = "ENM_TIPO_NTF", nullable = false)
    private TipoNotificacaoPlanoEnum tipoNotificacaoEnum;
}
