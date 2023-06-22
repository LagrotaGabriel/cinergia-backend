package br.com.backend.models.dto.pagamento.response;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse {
    private String dataCadastro;
    private String horaCadastro;
    private String dataPagamento;
    private String horaPagamento;
    private Double valorBruto;
    private Double valorLiquido;
    private String descricao;
    private String dataVencimento;
    private String linkCobranca;
    private String linkBoletoAsaas;
    private String formaPagamento;
    private String statusPagamento;
}
