package br.com.backend.modules.pagamento.models.entity;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.models.entity.id.PagamentoId;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import javax.persistence.*;

import br.com.backend.modules.plano.models.entity.PlanoEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PagamentoId.class)
@Table(name = "TB_SBS_PAGAMENTO")
public class PagamentoEntity {

    //TODO REVISAR MAPEAMENTO DO PLANO COMO OBJETO NO PAGAMENTO

    @Id
    @Comment("Chave primária do pagamento - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_PAGAMENTO_PGT", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Id
    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("Chave primária do pagamento - ID da empresa ao qual o pagamento faz parte")
    @JoinColumn(name = "COD_EMPRESA_PGT", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("ID do plano ao qual o pagamento faz parte")
    @JoinColumn(name = "COD_PLANO_PGT", nullable = false, updatable = false)
    private PlanoEntity plano;

    @Comment("Código de identificação do pagamento na integradora ASAAS")
    @Column(name = "COD_ASAAS_PGT", updatable = false, nullable = false, unique = true)
    private String asaasId;

    @Comment("Data em que o cadastro do pagamento foi realizado")
    @Column(name = "DT_DATACADASTRO_PGT", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do pagamento foi realizado")
    @Column(name = "HR_HORACADASTRO_PGT", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Data em que o pagamento foi realizado")
    @Column(name = "DT_DATACADASTRO_PGT", length = 10)
    private String dataPagamento;

    @Comment("Hora em que o pagamento foi realizado")
    @Column(name = "HR_HORACADASTRO_PGT", length = 18)
    private String horaPagamento;

    @Comment("Data de vencimento do pagamento")
    @Column(name = "DT_DATAVENCIMENTO_PGT", length = 10)
    private String dataVencimento;

    @Comment("Valor bruto do pagamento")
    @Column(name = "MON_BRUTO_PGT", nullable = false, scale = 2)
    private Double valorBruto;

    @Comment("Valor líquido do pagamento após dedução das taxas da integradora ASAAS")
    @Column(name = "MON_LIQUIDOASAAS_PGT", nullable = false, scale = 2)
    private Double valorLiquidoAsaas;

    @Comment("Total da somatória de taxas do pagamento. Fórmula: Taxa sistema + Taxa ASAAS")
    @Column(name = "MON_TAXAS_PGT", nullable = false, scale = 2)
    private Double taxaTotal;

    @Comment("Descrição do pagamento")
    @Column(name = "STR_DESCRICAO_PGT", nullable = false, length = 70)
    private String descricao;

    @Comment("Link de pagamento do pagamento")
    @Column(name = "URI_LINKPAGAMENTO_PGT")
    private String linkCobranca;

    @Comment("Link do boleto do pagamento")
    @Column(name = "URI_LINKBOLETO_PGT")
    private String linkBoletoAsaas;

    @Comment("Link do comprovante de pagamento do pagamento")
    @Column(name = "URI_LINKCOMPROVANTE_PGT")
    private String linkComprovante;

    @Enumerated(EnumType.STRING)
    @Comment("Forma de pagamento do pagamento: " +
            "0 - Boleto, " +
            "1 - Cartão de crédito, " +
            "2 - Cartão de débito, " +
            "3 - Pix, " +
            "4 - Definir")
    @Column(name = "ENM_FORMAPAGAMENTO_PGT", nullable = false)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do pagamento: " +
            "0 - Aprovado, " +
            "1 - Reprovado, " +
            "2 - Pendente, " +
            "3 - Atrasado, " +
            "4 - Cancelado")
    @Column(name = "ENM_STATUS_PGT", nullable = false)
    private StatusPagamentoEnum statusPagamento;

}
