package br.com.backend.modules.pagamento.models.responses;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Getter
@Setter
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse {
    private UUID uuid;
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
    private String linkComprovante;
    private String formaPagamento;
    private String statusPagamento;

    public PagamentoResponse constroiPagamentoResponse(PagamentoEntity pagamentoEntity) {
        log.info("Método de conversão de objeto do tipo PagamentoEntity para objeto do tipo PagamentoResponse acessado");

        log.info("Iniciando construção do objeto PagamentoResponse...");
        PagamentoResponse pagamentoResponse = PagamentoResponse.builder()
                .uuid(pagamentoEntity.getUuid())
                .dataCadastro(pagamentoEntity.getDataCriacao())
                .horaCadastro(pagamentoEntity.getHoraCriacao())
                .dataPagamento(pagamentoEntity.getDataPagamento())
                .horaPagamento(pagamentoEntity.getHoraPagamento())
                .valorBruto(pagamentoEntity.getValorBruto())
                .valorLiquido(pagamentoEntity.getValorLiquidoAsaas())
                .descricao(pagamentoEntity.getDescricao())
                .dataVencimento(pagamentoEntity.getDataVencimento())
                .linkCobranca(pagamentoEntity.getLinkCobranca())
                .linkBoletoAsaas(pagamentoEntity.getLinkBoletoAsaas())
                .linkComprovante(pagamentoEntity.getLinkComprovante())
                .formaPagamento(pagamentoEntity.getFormaPagamento().getDesc())
                .statusPagamento(pagamentoEntity.getStatusPagamento().getDesc())
                .build();
        log.debug("Objeto PagamentoResponse construído com sucesso. Retornando...");
        return pagamentoResponse;
    }
}
