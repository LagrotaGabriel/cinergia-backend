package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.enums.PeriodicidadeEnum;
import br.com.backend.proxy.plano.request.CycleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlanoTypeConverter {

    public PlanoResponse convertePlanoEntityParaPlanoResponse(PlanoEntity plano) {
        log.debug("Método de conversão de objeto do tipo PlanoEntity para objeto do tipo PlanoResponse acessado");

        log.debug("Iniciando construção do objeto PlanoResponse...");
        PlanoResponse planoResponse = PlanoResponse.builder()
                .id(plano.getId())
                .dataCadastro(plano.getDataCadastro())
                .horaCadastro(plano.getHoraCadastro())
                .dataInicio(plano.getDataInicio())
                .descricao(plano.getDescricao())
                .valor(plano.getValor())
                .formaPagamento(plano.getFormaPagamento())
                .statusPlano(plano.getStatusPlano())
                .periodicidade(plano.getPeriodicidade())
                .build();
        log.debug("Objeto PlanoResponse buildado com sucesso. Retornando...");
        return planoResponse;
    }

    public PlanoPageResponse converteListaDePlanosEntityParaPlanosResponse(Page<PlanoEntity> planosEntity) {
        log.debug("Método de conversão de planos do tipo Entity para planos do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo PlanoResponse...");
        List<PlanoResponse> planosResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de PlanoEntity obtida na busca para conversão para objetos do tipo " +
                "PlanoResponse...");
        for (PlanoEntity plano : planosEntity.getContent()) {
            PlanoResponse planoResponse = convertePlanoEntityParaPlanoResponse(plano);
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

    public CycleEnum transformaPeriodicidadeEnumEmCycleEnum(PeriodicidadeEnum periodicidadeEnum) {
        switch (periodicidadeEnum) {
            case SEMANAL: return CycleEnum.WEEKLY;
            case MENSAL: return CycleEnum.MONTHLY;
            case SEMESTRAL: return CycleEnum.SEMIANNUALY;
            default: return CycleEnum.YEARLY;
        }
    }

}
