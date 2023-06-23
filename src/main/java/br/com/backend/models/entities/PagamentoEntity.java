package br.com.backend.models.entities;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPagamentoEnum;
import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_pagamento")
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long idEmpresaResponsavel;
    @Column(nullable = false)
    private Long idPlanoResponsavel;
    @Column(nullable = false)
    private Long idClienteResponsavel;
    private String idAsaas;
    @Column(nullable = false)
    private String dataCadastro;
    @Column(nullable = false)
    private String horaCadastro;
    private String dataPagamento;
    private String horaPagamento;
    @Column(nullable = false)
    private Double valorBruto;
    private Double valorLiquidoAsaas;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private String dataVencimento;
    private String linkCobranca;
    private String linkBoletoAsaas;
    private String linkComprovante;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamentoEnum statusPagamento;

}
