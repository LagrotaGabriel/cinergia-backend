package br.com.backend.modules.pagamento.models.responses.page;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.responses.PagamentoResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoPageResponse {
    List<PagamentoResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public PagamentoPageResponse constroiPagamentoPageResponse(Page<PagamentoEntity> pagamentosEntity) {
        log.debug("Método de conversão de pagamentos do tipo Entity para pagamentos do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo PagamentoResponse...");
        List<PagamentoResponse> pagamentosResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de PagamentoEntity obtida na busca para conversão para objetos do tipo " +
                "PagamentoResponse...");
        for (PagamentoEntity pagamento : pagamentosEntity.getContent()) {
            PagamentoResponse pagamentoResponseResponse = new PagamentoResponse().constroiPagamentoResponse(pagamento);
            pagamentosResponse.add(pagamentoResponseResponse);
        }
        log.debug("Iteração finalizada com sucesso. Listagem de objetos do tipo PagamentoResponse preenchida");

        log.debug("Iniciando criação de objeto do tipo PagamentoPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        pagamentosEntity.getPageable();
        PagamentoPageResponse pagamentoPageResponse = PagamentoPageResponse.builder()
                .content(pagamentosResponse)
                .numberOfElements(pagamentosEntity.getNumberOfElements())
                .pageNumber(pagamentosEntity.getPageable().getPageNumber())
                .pageSize(pagamentosEntity.getPageable().getPageSize())
                .size(pagamentosEntity.getSize())
                .totalElements(pagamentosEntity.getTotalElements())
                .totalPages(pagamentosEntity.getTotalPages())
                .build();

        log.debug("Objeto do tipo PagamentoPageResponse criado com sucesso. Retornando objeto...");
        return pagamentoPageResponse;
    }
}
