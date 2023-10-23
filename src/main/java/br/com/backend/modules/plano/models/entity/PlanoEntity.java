package br.com.backend.modules.plano.models.entity;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PlanoId.class)
@Table(name = "tb_sbs_plano",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ASAAS_PLN", columnNames = {"cod_asaas_pln"}),
        })
public class PlanoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do plano - UUID")
    @Column(name = "cod_plano_pln", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Include
    @Comment("Chave primária do plano - ID da empresa ao qual o plano faz parte")
    @JoinColumn(name = "cod_empresa_pln",
            referencedColumnName = "cod_empresa_emp",
            nullable = false,
            updatable = false
    )
    @ToString.Exclude
    private EmpresaEntity empresa;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Include
    @Comment("ID do cliente ao qual o plano pertence")
    @JoinColumn(name = "cod_cliente_pln",
            referencedColumnName = "cod_cliente_cli",
            nullable = false,
            updatable = false)
    @JoinColumn(name = "cod_empresacliente_pln",
            referencedColumnName = "cod_empresa_cli",
            nullable = false,
            updatable = false)
    @ToString.Exclude
    private ClienteEntity cliente;

    @Comment("Código de identificação do plano na integradora ASAAS")
    @Column(name = "cod_asaas_pln", nullable = false, updatable = false)
    private String asaasId;

    @Comment("Data em que o cadastro do plano foi realizado")
    @Column(name = "dt_datacadastro_pln", nullable = false, updatable = false, length = 10)
    private String dataCriacao;

    @Comment("Hora em que o cadastro do plano foi realizado")
    @Column(name = "hr_horacadastro_pln", nullable = false, updatable = false, length = 18)
    private String horaCriacao;

    @Comment("Data de vencimento na qual as faturas do plano serão geradas")
    @Column(name = "dt_datavencimento_pln", length = 10)
    private String dataVencimento;

    @Comment("Data de agendamento para a remoção do plano. Só deve estar preenchido caso o plano tenha sido removido " +
            "mas possua algum pagamento ativo")
    @Column(name = "dt_dataagendamentoremocao_pln", length = 10)
    private String dataAgendamentoRemocao;

    @Comment("Data de início da primeira fatura do plano")
    @Column(name = "dt_datainicio_pln", nullable = false, updatable = false, length = 10)
    private String dataInicio;

    @Comment("Descrição do plano")
    @Column(name = "str_descricao_pln", nullable = false, length = 70)
    private String descricao;

    @Comment("Valor do plano")
    @Column(name = "mon_valor_pln", nullable = false, scale = 2)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Comment("Forma de pagamento do plano: " +
            "0 - Boleto, " +
            "1 - Cartão de crédito, " +
            "2 - Cartão de débito, " +
            "3 - Pix, " +
            "4 - Definir")
    @Column(name = "enm_formapagamento_pln", nullable = false)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do plano: " +
            "0 - Ativo, " +
            "1 - Inativo, " +
            "2 - Removido")
    @Column(name = "enm_status_pln", nullable = false)
    private StatusPlanoEnum statusPlano;

    @Enumerated(EnumType.STRING)
    @Comment("Periodicidade de pagamento do plano: " +
            "0 - Semanal, " +
            "1 - Mensal, " +
            "2 - Semestral, " +
            "3 - Anual")
    @Column(name = "enm_periodicidade_pgt", nullable = false)
    private PeriodicidadeEnum periodicidade;

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de notificações do plano")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @CollectionTable(name = "tb_sbs_plano_notificacoes",
            joinColumns = {
                    @JoinColumn(name = "cod_empresa_pln",
                            referencedColumnName = "cod_empresa_pln",
                            foreignKey = @ForeignKey(name = "FK_EMPRESA_PLANO")),
                    @JoinColumn(name = "cod_plano_pln",
                            referencedColumnName = "cod_plano_pln",
                            foreignKey = @ForeignKey(name = "FK_COD_PLANO"))
            })
    @ElementCollection(fetch = FetchType.EAGER, targetClass = NotificacaoEnum.class)
    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @Comment("Lista de pagamentos do plano")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = PagamentoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_sbs_plano_pagamentos",
            uniqueConstraints = {
                    @UniqueConstraint(name = "UK_PAGAMENTO_PLANO", columnNames = {"cod_empresa_pgt"}),
                    @UniqueConstraint(name = "UK_PAGAMENTO_PLANO", columnNames = {"cod_pagamento_pgt"})
            },
            joinColumns = {
                    @JoinColumn(name = "cod_empresa_pln",
                            referencedColumnName = "cod_empresa_pln",
                            foreignKey = @ForeignKey(name = "FK_EMPRESA_PLANO")),
                    @JoinColumn(name = "cod_plano_pln",
                            referencedColumnName = "cod_plano_pln",
                            foreignKey = @ForeignKey(name = "FK_COD_PLANO")),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "cod_empresa_pgt",
                            referencedColumnName = "cod_empresa_pgt"),
                    @JoinColumn(name = "cod_pagamento_pgt",
                            referencedColumnName = "cod_pagamento_pgt")
            }
    )
    private List<PagamentoEntity> pagamentos = new ArrayList<>();

    public PlanoEntity constroiPlanoEntityParaCriacao(EmpresaEntity empresaSessao,
                                                      String asaasId,
                                                      ClienteEntity cliente,
                                                      PlanoRequest planoRequest) {
        return PlanoEntity.builder()
                .empresa(empresaSessao)
                .cliente(cliente)
                .asaasId(asaasId)
                .dataCriacao(LocalDate.now().toString())
                .horaCriacao(LocalTime.now().toString())
                .dataVencimento(null)
                .dataAgendamentoRemocao(null)
                .dataInicio(planoRequest.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(StatusPlanoEnum.INATIVO)
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .pagamentos(new ArrayList<>())
                .build();
    }

    public PlanoEntity atualizaEntidadeComAtributosRequest(PlanoEntity planoEncontrado, PlanoRequest planoRequest) {
        return PlanoEntity.builder()
                .uuid(planoEncontrado.getUuid())
                .empresa(planoEncontrado.getEmpresa())
                .cliente(planoEncontrado.getCliente())
                .asaasId(planoEncontrado.getAsaasId())
                .dataCriacao(planoEncontrado.getDataCriacao())
                .horaCriacao(planoEncontrado.getHoraCriacao())
                .dataVencimento(planoEncontrado.getDataVencimento())
                .dataAgendamentoRemocao(planoEncontrado.getDataAgendamentoRemocao())
                .dataInicio(planoEncontrado.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(planoEncontrado.getStatusPlano())
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .pagamentos(planoEncontrado.getPagamentos())
                .build();
    }

    public void addPagamento(PagamentoEntity pagamentoEntity) {
        this.pagamentos.add(pagamentoEntity);
    }

    public PagamentoEntity obtemUltimoPagamentoPersistido() {
        int indicePagamento = this.getPagamentos().size() - 1;
        return this.getPagamentos().get(indicePagamento);
    }
}
