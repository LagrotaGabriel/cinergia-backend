package br.com.backend.modules.transferencia.models.dto.response.page;

import br.com.backend.modules.transferencia.models.dto.response.TransferenciaResponse;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaPageResponse {
    List<TransferenciaResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public TransferenciaPageResponse constroiObjeto(Page<TransferenciaEntity> transferenciasEntity) {
        log.info("Método de conversão de transferências do tipo Entity para transferências do tipo Response acessado");

        log.info("Criando lista vazia de objetos do tipo TransferenciaResponse...");
        List<TransferenciaResponse> transferenciasResponse = new ArrayList<>();

        log.info("Iniciando iteração da lista de TransferenciaEntity obtida na busca para conversão para " +
                "objetos do tipo TransferenciaResponse...");
        for (TransferenciaEntity transferencia : transferenciasEntity.getContent()) {
            TransferenciaResponse transferenciaResponse = new TransferenciaResponse()
                    .constroiTransferenciaResponse(transferencia);
            transferenciasResponse.add(transferenciaResponse);
        }
        log.info("Iteração finalizada com sucesso. Listagem de objetos do tipo TransferenciaResponse preenchida");

        log.info("Iniciando criação de objeto do tipo TransferenciaPageResponse, que possui todas as " +
                "informações referentes ao conteúdo da página e à paginação...");
        transferenciasEntity.getPageable();
        TransferenciaPageResponse transferenciaPageResponse = TransferenciaPageResponse.builder()
                .content(transferenciasResponse)
                .numberOfElements(transferenciasEntity.getNumberOfElements())
                .pageNumber(transferenciasEntity.getPageable().getPageNumber())
                .pageSize(transferenciasEntity.getPageable().getPageSize())
                .size(transferenciasEntity.getSize())
                .totalElements(transferenciasEntity.getTotalElements())
                .totalPages(transferenciasEntity.getTotalPages())
                .build();

        log.info("Objeto do tipo TransferenciaPageResponse criado com sucesso. Retornando objeto...");
        return transferenciaPageResponse;
    }
}
