package br.com.backend.modules.plano.models.entity;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PlanoId.class)
@Table(name = "TB_SBS_PLANO")
public class PlanoEntity {

    //TODO REVISAR MAPEAMENTO DO CLIENTE COMO OBJETO NO PLANO

    @Id
    @Comment("Chave primária do plano - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(table = "TB_SBS_PLANO", name = "COD_PLANO_PLN", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Id
    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("Chave primária do plano - ID da empresa ao qual o plano faz parte")
    @JoinColumn(table = "TB_SBS_PLANO", name = "COD_EMPRESA_PLN", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("ID do cliente ao qual o plano pertence")
    @JoinColumn(table = "TB_SBS_PLANO", name = "COD_CLIENTE_PLN", nullable = false, updatable = false)
    private ClienteEntity cliente;

    @Comment("Código de identificação do plano na integradora ASAAS")
    @Column(table = "TB_SBS_PLANO", name = "COD_ASAAS_PLN", updatable = false, nullable = false, unique = true)
    private String asaasId;

    @Comment("Data em que o cadastro do plano foi realizado")
    @Column(table = "TB_SBS_PLANO", name = "DT_DATACADASTRO_PLN", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do plano foi realizado")
    @Column(table = "TB_SBS_PLANO", name = "HR_HORACADASTRO_PLN", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data de vencimento na qual as faturas do plano serão geradas")
    @Column(table = "TB_SBS_PLANO", name = "DT_DATAVENCIMENTO_PLN", nullable = false, length = 10)
    private String dataVencimento;

    @Comment("Data de início da primeira fatura do plano")
    @Column(table = "TB_SBS_PLANO", name = "DT_DATAINICIO_PLN", nullable = false, length = 10)
    private String dataInicio;

    @Comment("Descrição do plano")
    @Column(table = "TB_SBS_PLANO", name = "STR_DESCRICAO_PLN", nullable = false, length = 70)
    private String descricao;

    @Comment("Valor do plano")
    @Column(table = "TB_SBS_PLANO", name = "MON_VALOR_PLN", nullable = false, scale = 2)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Comment("Forma de pagamento do plano: " +
            "0 - Boleto, " +
            "1 - Cartão de crédito, " +
            "2 - Cartão de débito, " +
            "3 - Pix, " +
            "4 - Definir")
    @Column(table = "TB_SBS_PLANO", name = "ENM_FORMAPAGAMENTO_PLN", nullable = false)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do plano: " +
            "0 - Ativo, " +
            "1 - Inativo, " +
            "2 - Removido")
    @Column(table = "TB_SBS_PLANO", name = "ENM_STATUS_PLN", nullable = false)
    private StatusPlanoEnum statusPlano;

    @Enumerated(EnumType.STRING)
    @Comment("Periodicidade de pagamento do plano: " +
            "0 - Semanal, " +
            "1 - Mensal, " +
            "2 - Semestral, " +
            "3 - Anual")
    @Column(table = "TB_SBS_PLANO", name = "ENM_PERIODICIDADE_PGT", nullable = false)
    private PeriodicidadeEnum periodicidade;

    @Comment("Lista de notificações do plano")
    @Column(table = "TB_SBS_PLANO", name = "LST_NOTIFICACOES_PLN")
    @CollectionTable(name = "NOTIFICACOES")
    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de pagamentos do plano")
    @Column(table = "TB_SBS_PLANO", name = "LST_PAGAMENTOS_PLN")
    @OneToMany(targetEntity = PagamentoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PagamentoEntity> pagamentos = new ArrayList<>();

}
