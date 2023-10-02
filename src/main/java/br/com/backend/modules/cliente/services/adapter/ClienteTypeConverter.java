package br.com.backend.modules.cliente.services.adapter;

import br.com.backend.modules.cliente.models.dto.response.ClientePageResponse;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClienteTypeConverter {

    public ClienteResponse converteClienteEntityParaClienteResponse(ClienteEntity cliente) {
        log.debug("Método de conversão de objeto do tipo ClienteEntity para objeto do tipo ClienteResponse acessado");

        log.debug("Iniciando construção do objeto ClienteResponse...");
        ClienteResponse clienteResponse = ClienteResponse.builder()
                .id(cliente.getId())
                .dataCadastro(cliente.getDataCadastro())
                .horaCadastro(cliente.getHoraCadastro())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .cpfCnpj(cliente.getCpfCnpj())
                .observacoes(cliente.getObservacoes())
                .statusCliente(cliente.getStatusCliente())
                .dataNascimento(cliente.getDataNascimento())
                .tipoPessoa(cliente.getTipoPessoa())
                .acessoSistema(cliente.getAcessoSistema())
                .endereco(cliente.getEndereco())
                .fotoPerfil(cliente.getFotoPerfil())
                .telefones(cliente.getTelefones())
                .build();
        log.debug("Objeto ClienteResponse buildado com sucesso. Retornando...");
        return clienteResponse;
    }

    public ClientePageResponse converteListaDeClientesEntityParaClientesResponse(Page<ClienteEntity> clientesEntity) {
        log.debug("Método de conversão de clientes do tipo Entity para clientes do tipo Response acessado");

        log.debug("Criando lista vazia de objetos do tipo ClienteResponse...");
        List<ClienteResponse> clientesResponse = new ArrayList<>();

        log.debug("Iniciando iteração da lista de ClienteEntity obtida na busca para conversão para objetos do tipo " +
                "ClienteResponse...");
        for (ClienteEntity cliente : clientesEntity.getContent()) {
            ClienteResponse clienteResponse = converteClienteEntityParaClienteResponse(cliente);
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
