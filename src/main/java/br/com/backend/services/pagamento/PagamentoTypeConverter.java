package br.com.backend.services.pagamento;

import br.com.backend.models.dto.pagamento.response.PagamentoPageResponse;
import br.com.backend.models.dto.pagamento.response.PagamentoResponse;
import br.com.backend.models.entities.PagamentoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PagamentoTypeConverter {

    public PagamentoPageResponse converteListaDePagamentosEntityParaPagamentosResponse(Page<PagamentoEntity> pagamentoEntities) {
        log.debug("Método de conversão de pagamentos do tipo Entity para pagamentos do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo PagamentoResponse...");
        List<PagamentoResponse> pagamentosResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de PagamentoEntity obtida na busca para conversão para objetos do tipo " +
                "PagamentoResponse...");
        for (PagamentoEntity pagamento : pagamentoEntities.getContent()) {
            PagamentoResponse pagamentoResponse = convertePagamentoEntityParaPagamentoResponse(pagamento);
            pagamentosResponse.add(pagamentoResponse);
        }
        log.debug("Iteração finalizada com sucesso. Listagem de objetos do tipo PagamentoResponse preenchida");

        log.debug("Iniciando criação de objeto do tipo PagamentoPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        pagamentoEntities.getPageable();
        PagamentoPageResponse pagamentoPageResponse = PagamentoPageResponse.builder()
                .content(pagamentosResponse)
                .numberOfElements(pagamentoEntities.getNumberOfElements())
                .pageNumber(pagamentoEntities.getPageable().getPageNumber())
                .pageSize(pagamentoEntities.getPageable().getPageSize())
                .size(pagamentoEntities.getSize())
                .totalElements(pagamentoEntities.getTotalElements())
                .totalPages(pagamentoEntities.getTotalPages())
                .build();

        log.debug("Objeto do tipo PagamentoPageResponse criado com sucesso. Retornando objeto...");
        return pagamentoPageResponse;
    }

    public PagamentoResponse convertePagamentoEntityParaPagamentoResponse(PagamentoEntity pagamento) {
        log.debug("Método de conversão de objeto do tipo PagamentoEntity para objeto do tipo PagamentoResponse acessado");

        log.debug("Iniciando construção do objeto PagamentoResponse...");
        PagamentoResponse pagamentoResponse = PagamentoResponse.builder()
                .dataCadastro(pagamento.getDataCadastro())
                .horaCadastro(pagamento.getHoraCadastro())
                .dataPagamento(pagamento.getDataPagamento())
                .horaPagamento(pagamento.getHoraPagamento())
                .valorBruto(pagamento.getValorBruto())
                .valorLiquido(null) //TODO CALCULAR VALOR LÍQUIDO
                .descricao(pagamento.getDescricao())
                .dataVencimento(pagamento.getDataVencimento())
                .linkCobranca(pagamento.getLinkCobranca())
                .linkBoletoAsaas(pagamento.getLinkBoletoAsaas())
                .formaPagamento(pagamento.getFormaPagamento().getDesc())
                .statusPagamento(pagamento.getStatusPagamento().getDesc())
                .build();
        log.debug("Objeto PagamentoResponse buildado com sucesso. Retornando...");
        return pagamentoResponse;
    }
}
