package br.com.backend.modules.cliente.models.dto.response.page;

import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientePageResponse {
    List<ClienteResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;

    public ClientePageResponse constroiClientePageResponse(Page<ClienteEntity> clientesEntity) {
        log.debug("Método de conversão de clientes do tipo Entity para clientes do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo ClienteResponse...");
        List<ClienteResponse> clientesResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de ClienteEntity obtida na busca para conversão para objetos do tipo " +
                "ClienteResponse...");
        for (ClienteEntity cliente : clientesEntity.getContent()) {
            ClienteResponse clienteResponse = new ClienteResponse().constroiClienteResponse(cliente);
            clientesResponse.add(clienteResponse);
        }
        log.debug("Iteração finalizada com sucesso. Listagem de objetos do tipo ClienteResponse preenchida");

        log.debug("Iniciando criação de objeto do tipo ClientePageResponse, que possui todas as informações referentes " +
                "ao conteúdo da página e à paginação...");
        clientesEntity.getPageable();
        ClientePageResponse clientePageResponse = ClientePageResponse.builder()
                .content(clientesResponse)
                .numberOfElements(clientesEntity.getNumberOfElements())
                .pageNumber(clientesEntity.getPageable().getPageNumber())
                .pageSize(clientesEntity.getPageable().getPageSize())
                .size(clientesEntity.getSize())
                .totalElements(clientesEntity.getTotalElements())
                .totalPages(clientesEntity.getTotalPages())
                .build();

        log.debug("Objeto do tipo ClientePageResponse criado com sucesso. Retornando objeto...");
        return clientePageResponse;
    }
}
