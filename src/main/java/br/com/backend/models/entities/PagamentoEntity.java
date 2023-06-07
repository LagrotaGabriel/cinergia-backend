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

    private String dataCadastro;
    private String horaCadastro;
    private Double valorBruto;
    private Double valorTaxaIntegradora;
    private Double valorTaxaSistema;
    private String descricao;
    private String dataVencimento;
    private String finalCartao;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamentoEnum statusPagamento;

}
