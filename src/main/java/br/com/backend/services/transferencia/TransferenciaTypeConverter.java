package br.com.backend.services.transferencia;

import br.com.backend.models.dto.transferencia.response.TransferenciaPageResponse;
import br.com.backend.models.dto.transferencia.response.TransferenciaResponse;
import br.com.backend.models.entities.TransferenciaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransferenciaTypeConverter {

    public TransferenciaResponse converteTransferenciaEntityParaTransferenciaResponse(TransferenciaEntity transferencia) {
        log.debug("Método de conversão de objeto do tipo TransferenciaEntity para objeto do tipo TransferenciaResponse acessado");

        log.debug("Iniciando construção do objeto TransferenciaResponse...");
        TransferenciaResponse transferenciaResponse = TransferenciaResponse.builder()
                .dataCadastro(transferencia.getDataCadastro())
                .horaCadastro(transferencia.getHoraCadastro())
                .descricao(transferencia.getDescricao())
                .valor(transferencia.getValor())
                .chavePix(transferencia.getChavePix())
                .tipoChavePix(transferencia.getTipoChavePix())
                .build();
        log.debug("Objeto TransferenciaResponse buildado com sucesso. Retornando...");
        return transferenciaResponse;
    }

    public TransferenciaPageResponse converteListaDeTransferenciasEntityParaTransferenciasResponse(Page<TransferenciaEntity> transferenciasEntity) {
        log.debug("Método de conversão de transferencias do tipo Entity para transferencias do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo TransferenciaResponse...");
        List<TransferenciaResponse> transferenciaResponses = new ArrayList<>();

        log.debug("Iniciando iteração da lista de TransferenciaEntity obtida na busca para conversão para objetos do tipo " +
                "TransferenciaResponse...");
        for (TransferenciaEntity transferencia : transferenciasEntity.getContent()) {
            TransferenciaResponse transferenciaResponse = converteTransferenciaEntityParaTransferenciaResponse(transferencia);
            transferenciaResponses.add(transferenciaResponse);
        }
        log.debug("Iteração finalizada com sucesso. Listagem de objetos do tipo TransferenciaResponse preenchida");

        log.debug("Iniciando criação de objeto do tipo TransferenciaPageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        transferenciasEntity.getPageable();
        TransferenciaPageResponse transferenciaPageResponsePageResponse = TransferenciaPageResponse.builder()
                .content(transferenciaResponses)
                .numberOfElements(transferenciasEntity.getNumberOfElements())
                .pageNumber(transferenciasEntity.getPageable().getPageNumber())
                .pageSize(transferenciasEntity.getPageable().getPageSize())
                .size(transferenciasEntity.getSize())
                .totalElements(transferenciasEntity.getTotalElements())
                .totalPages(transferenciasEntity.getTotalPages())
                .build();

        log.debug("Objeto do tipo TransferenciaPageResponse criado com sucesso. Retornando objeto...");
        return transferenciaPageResponsePageResponse;
    }

}
