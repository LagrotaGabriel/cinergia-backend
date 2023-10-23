package br.com.backend.modules.notificacao.models.entity;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.notificacao.models.entity.id.NotificacaoId;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(NotificacaoId.class)
@Table(name = "tb_sbs_notificacao")
public class NotificacaoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da notificação - UUID")
    @Column(name = "cod_notificacao_ntf", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária da notificação - ID da empresa ao qual a notificação faz parte")
    @JoinColumn(name = "cod_empresa_ntf", referencedColumnName = "cod_empresa_emp", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Data em que o cadastro da notificação foi realizado")
    @Column(name = "dt_datacadastro_ntf", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da notificação foi realizado")
    @Column(name = "hr_horacadastro_ntf", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Descrição da notificação")
    @Column(name = "str_descricao_ntf", nullable = false, length = 70)
    private String descricao;

    @Comment("Link da notificação")
    @Column(name = "uri_link_ntf", length = 70)
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

    public NotificacaoEntity criaNovaNotificacaoEntity(EmpresaEntity empresaSessao,
                                                       String descricao,
                                                       String uri,
                                                       TipoNotificacaoPlanoEnum tipoNotificacao) {
        return NotificacaoEntity.builder()
                .empresa(empresaSessao)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .descricao(descricao)
                .uri(uri)
                .tipoNotificacaoEnum(tipoNotificacao)
                .lida(false)
                .build();
    }
}
