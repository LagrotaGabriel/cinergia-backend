package br.com.backend.modules.plano.models.dto.response.page;

import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
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
public class PlanoPageResponse {
    List<PlanoResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public PlanoPageResponse constroiPlanoPageResponse(Page<PlanoEntity> planosEntity) {
        log.debug("Método de conversão de planos do tipo Entity para planos do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo PlanoResponse...");
        List<PlanoResponse> planosResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de PlanoEntity obtida na busca para conversão para objetos do tipo " +
                "PlanoResponse...");
        for (PlanoEntity plano : planosEntity.getContent()) {
            PlanoResponse planoResponse = new PlanoResponse().constroiPlanoResponse(plano);
            planosResponse.add(planoResponse);
        }
        log.debug("Iteração finalizada com sucesso. Listagem de objetos do tipo PlanoResponse preenchida");

        log.debug("Iniciando criação de objeto do tipo PlanoPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        planosEntity.getPageable();
        PlanoPageResponse planoPageResponse = PlanoPageResponse.builder()
                .content(planosResponse)
                .numberOfElements(planosEntity.getNumberOfElements())
                .pageNumber(planosEntity.getPageable().getPageNumber())
                .pageSize(planosEntity.getPageable().getPageSize())
                .size(planosEntity.getSize())
                .totalElements(planosEntity.getTotalElements())
                .totalPages(planosEntity.getTotalPages())
                .build();

        log.debug("Objeto do tipo PlanoPageResponse criado com sucesso. Retornando objeto...");
        return planoPageResponse;
    }
}
