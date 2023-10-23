package br.com.backend.modules.pagamento.models.entity;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.models.entity.id.PagamentoId;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PagamentoId.class)
@Table(name = "tb_sbs_pagamento",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ASAAS_PGT", columnNames = {"cod_asaas_pgt"}),
        })
@EntityListeners(AuditingEntityListener.class)
public class PagamentoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do pagamento - UUID")
    @Column(name = "cod_pagamento_pgt", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Include
    @Comment("Chave primária do pagamento - ID da empresa ao qual o pagamento faz parte")
    @JoinColumn(name = "cod_empresa_pgt",
            referencedColumnName = "cod_empresa_emp",
            nullable = false,
            updatable = false)
    @ToString.Exclude
    private EmpresaEntity empresa;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Include
    @Comment("ID do plano ao qual o pagamento faz parte")
    @JoinColumn(name = "cod_plano_pgt",
            referencedColumnName = "cod_plano_pln",
            nullable = false,
            updatable = false)
    @JoinColumn(name = "cod_empresaplano_pgt",
            referencedColumnName = "cod_empresa_pln",
            nullable = false,
            updatable = false)
    @ToString.Exclude
    private PlanoEntity plano;

    @Comment("Código de identificação do pagamento na integradora ASAAS")
    @Column(name = "cod_asaas_pgt", updatable = false, nullable = false)
    private String asaasId;

    @Comment("Data em que o cadastro do pagamento foi realizado")
    @Column(name = "dt_datacadastro_pgt", nullable = false, updatable = false, length = 10)
    private String dataCriacao;

    @Comment("Hora em que o cadastro do pagamento foi realizado")
    @Column(name = "hr_horacadastro_pgt", nullable = false, updatable = false, length = 18)
    private String horaCriacao;


    @Comment("Data em que o pagamento foi realizado")
    @Column(name = "dt_datapagamento_pgt", length = 10)
    private String dataPagamento;

    @Comment("Hora em que o pagamento foi realizado")
    @Column(name = "hr_horapagamento_pgt", length = 18)
    private String horaPagamento;

    @Comment("Data de vencimento do pagamento")
    @Column(name = "dt_datavencimento_pgt", length = 10)
    private String dataVencimento;

    @Comment("Valor bruto do pagamento")
    @Column(name = "mon_bruto_pgt", nullable = false, scale = 2)
    private Double valorBruto;

    @Comment("Valor líquido do pagamento após dedução das taxas da integradora ASAAS")
    @Column(name = "mon_liquidoasaas_pgt", nullable = false, scale = 2)
    private Double valorLiquidoAsaas;

    @Comment("Total da somatória de taxas do pagamento. Fórmula: Taxa sistema + Taxa ASAAS")
    @Column(name = "mon_taxas_pgt", nullable = false, scale = 2)
    private Double taxaTotal;

    @Comment("Descrição do pagamento")
    @Column(name = "str_descricao_pgt", nullable = false, length = 70)
    private String descricao;

    @Comment("Link de pagamento do pagamento")
    @Column(name = "uri_linkpagamento_pgt")
    private String linkCobranca;

    @Comment("Link do boleto do pagamento")
    @Column(name = "uri_linkboleto_pgt")
    private String linkBoletoAsaas;

    @Comment("Link do comprovante de pagamento do pagamento")
    @Column(name = "uri_linkcomprovante_pgt")
    private String linkComprovante;

    @Enumerated(EnumType.STRING)
    @Comment("Forma de pagamento do pagamento: " +
            "0 - Boleto, " +
            "1 - Cartão de crédito, " +
            "2 - Cartão de débito, " +
            "3 - Pix, " +
            "4 - Definir")
    @Column(name = "enm_formapagamento_pgt", nullable = false)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do pagamento: " +
            "0 - Aprovado, " +
            "1 - Reprovado, " +
            "2 - Pendente, " +
            "3 - Atrasado, " +
            "4 - Cancelado")
    @Column(name = "enm_status_pgt", nullable = false)
    private StatusPagamentoEnum statusPagamento;

    public PagamentoEntity constroiPagamentoEntityParaCriacao(EmpresaEntity empresaSessao,
                                                              PlanoEntity plano,
                                                              PagamentoWebHookRequest pagamentoWebHookRequest) {
        log.info("Iniciando construção do objeto PagamentoEntity com valores recebidos pelo ASAAS...");
        return PagamentoEntity.builder()
                .empresa(empresaSessao)
                .plano(plano)
                .asaasId(pagamentoWebHookRequest.getId())
                .dataCriacao(LocalDate.now().toString())
                .horaCriacao(LocalTime.now().toString())
                .dataPagamento(null)
                .horaPagamento(null)
                .dataVencimento(pagamentoWebHookRequest.getDueDate())
                .valorBruto(pagamentoWebHookRequest.getValue())
                .valorLiquidoAsaas(pagamentoWebHookRequest.getNetValue())
                .taxaTotal(0.0)
                .descricao(pagamentoWebHookRequest.getDescription())
                .linkCobranca(pagamentoWebHookRequest.getInvoiceUrl())
                .linkBoletoAsaas(pagamentoWebHookRequest.getBankSlipUrl())
                .linkComprovante(pagamentoWebHookRequest.getTransactionReceiptUrl())
                .formaPagamento(FormaPagamentoEnum.valueOf(pagamentoWebHookRequest
                        .getBillingType().getFormaPagamentoResumida()))
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build();

    }

    public PagamentoEntity constroiObjetoPagamentoParaPagamentoConfirmado(PagamentoEntity pagamentoEncontrado,
                                                                          PagamentoWebHookRequest pagamentoWebHookRequest) {

        return PagamentoEntity.builder()
                .uuid(pagamentoEncontrado.getUuid())
                .empresa(pagamentoEncontrado.getEmpresa())
                .plano(pagamentoEncontrado.getPlano())
                .asaasId(pagamentoEncontrado.getAsaasId())
                .dataCriacao(pagamentoEncontrado.getDataCriacao())
                .horaCriacao(pagamentoEncontrado.getHoraCriacao())
                .dataPagamento(LocalDate.now().toString())
                .horaPagamento(LocalTime.now().toString())
                .dataVencimento(pagamentoWebHookRequest.getDueDate())
                .valorBruto(pagamentoWebHookRequest.getValue())
                .valorLiquidoAsaas(pagamentoWebHookRequest.getNetValue())
                .descricao(pagamentoWebHookRequest.getDescription())
                .linkCobranca(pagamentoWebHookRequest.getInvoiceUrl())
                .linkBoletoAsaas(pagamentoWebHookRequest.getBankSlipUrl())
                .linkComprovante(pagamentoWebHookRequest.getTransactionReceiptUrl())
                .formaPagamento(FormaPagamentoEnum.valueOf(pagamentoWebHookRequest
                        .getBillingType().getFormaPagamentoResumida()))
                .statusPagamento(StatusPagamentoEnum.APROVADO)
                .build();
    }
}
