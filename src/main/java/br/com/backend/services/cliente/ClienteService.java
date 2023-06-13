package br.com.backend.services.cliente;

import br.com.backend.models.dto.cliente.request.ClienteRequest;
import br.com.backend.models.dto.cliente.response.ClientePageResponse;
import br.com.backend.models.dto.cliente.response.ClienteResponse;
import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.models.entities.ExclusaoEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.enums.PerfilEnum;
import br.com.backend.repositories.cliente.ClienteRepository;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ClienteService {

    @Autowired
    ClienteValidationService clienteValidationService;

    @Autowired
    ClienteTypeConverter clienteTypeConverter;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    public ClienteResponse criaNovoCliente(EmpresaEntity empresaLogada, ClienteRequest clienteRequest) {

        log.debug("Método de serviço de criação de novo cliente acessado");

        log.debug("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaNovoCliente(empresaLogada, clienteRequest);

        Set<PerfilEnum> acessoCliente = new HashSet<>();
        acessoCliente.add(PerfilEnum.CLIENTE);

        log.debug("Iniciando criação do objeto ClienteEntity...");
        ClienteEntity clienteEntity = ClienteEntity.builder()
                .idEmpresaResponsavel(empresaLogada.getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .nome(clienteRequest.getNome() != null ? clienteRequest.getNome().toUpperCase() : null)
                .email(clienteRequest.getEmail() != null ? clienteRequest.getEmail().toLowerCase() : null)
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .statusCliente(clienteRequest.getStatusCliente())
                .dataNascimento(clienteRequest.getDataNascimento())
                .tipoPessoa(clienteRequest.getTipoPessoa())
                .acessoSistema(clienteRequest.getAcessoSistema() != null
                        ? AcessoSistemaEntity.builder()
                        .senha(new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha()))
                        .perfis(acessoCliente)
                        .build()
                        : null)
                .exclusao(null)
                .endereco(clienteRequest.getEndereco() == null
                        ? null
                        : EnderecoEntity.builder()
                        .logradouro(clienteRequest.getEndereco().getLogradouro())
                        .numero(clienteRequest.getEndereco().getNumero())
                        .bairro(clienteRequest.getEndereco().getBairro())
                        .codigoPostal(clienteRequest.getEndereco().getCodigoPostal())
                        .cidade(clienteRequest.getEndereco().getCidade())
                        .complemento(clienteRequest.getEndereco().getComplemento())
                        .estado(clienteRequest.getEndereco().getEstado())
                        .build())
                .telefones(clienteRequest.getTelefones())
                .planos(new ArrayList<>())
                .cartoes(new ArrayList<>())
                .build();
        log.debug("Objeto clienteEntity criado com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEntity);

        log.debug("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
        ClienteResponse clienteResponse = clienteTypeConverter.converteClienteEntityParaClienteResponse(clientePersistido);

        log.info("Cliente criado com sucesso");
        return clienteResponse;
    }

    public ClientePageResponse realizaBuscaPaginadaPorClientes(EmpresaEntity empresaLogada,
                                                               Pageable pageable,
                                                               String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de clientes acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de clientes");
        Page<ClienteEntity> clientePage = campoBusca != null && !campoBusca.isEmpty()
                ? clienteRepository.buscaPorClientesTypeAhead(pageable, campoBusca, empresaLogada.getId())
                : clienteRepository.buscaPorClientes(pageable, empresaLogada.getId());

        log.debug("Busca de clientes por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        ClientePageResponse clientePageResponse = clienteTypeConverter.converteListaDeClientesEntityParaClientesResponse(clientePage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de clientes foi realizada com sucesso");
        return clientePageResponse;
    }

    public void validaSeCpfCnpjJaExiste(String cpfCnpj, Long idEmpresa) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");
        if (clienteRepositoryImpl.implementaBuscaPorCpfCnpjIdentico(cpfCnpj, idEmpresa).isPresent()) {
            String mensagemErro = cpfCnpj.length() == 11 ? "O CPF informado já existe" : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public ClienteResponse removeCliente(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de remoção de cliente acessado");

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());

        log.debug("Iniciando acesso ao método de validação de exclusão de cliente que já foi excluído...");
        validaSeClienteEstaExcluido(clienteEncontrado,
                "O cliente selecionado já foi excluído");

        log.debug("Atualizando objeto ExclusaoCliente do cliente com dados referentes à sua exclusão...");
        ExclusaoEntity exclusao = ExclusaoEntity.builder()
                .dataExclusao(LocalDate.now().toString())
                .horaExclusao(LocalTime.now().toString())
                .build();

        clienteEncontrado.setExclusao(exclusao);
        log.debug("Objeto ExclusaoCliente do cliente de id {} setado com sucesso", id);

        log.debug("Persistindo cliente excluído no banco de dados...");
        ClienteEntity clienteExcluido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.info("Cliente excluído com sucesso");
        return ClienteResponse.builder()
                .id(clienteExcluido.getId())
                .dataCadastro(clienteExcluido.getDataCadastro())
                .horaCadastro(clienteExcluido.getHoraCadastro())
                .dataNascimento(clienteExcluido.getDataNascimento())
                .nome(clienteExcluido.getNome())
                .cpfCnpj(clienteExcluido.getCpfCnpj())
                .email(clienteExcluido.getEmail())
                .statusCliente(clienteExcluido.getStatusCliente())
                .tipoPessoa(clienteExcluido.getTipoPessoa())
                .acessoSistema(clienteExcluido.getAcessoSistema())
                .endereco(clienteExcluido.getEndereco())
                .telefones(clienteExcluido.getTelefones())
                .build();
    }

    public void removeClientesEmMassa(EmpresaEntity empresaLogada, List<Long> ids) {
        log.debug("Método de serviço de remoção de cliente acessado");

        List<ClienteEntity> clientesEncontrados = new ArrayList<>();

        for (Long id : ids) {
            log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
            ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());
            clientesEncontrados.add(clienteEncontrado);
        }

        log.debug("Iniciando acesso ao método de validação de exclusão de cliente que já foi excluído...");
        for (ClienteEntity cliente : clientesEncontrados) {
            validaSeClienteEstaExcluido(cliente,
                    "O cliente selecionado já foi excluído");
            log.debug("Atualizando objeto ExclusaoCliente do cliente com dados referentes à sua exclusão...");
            ExclusaoEntity exclusao = ExclusaoEntity.builder()
                    .dataExclusao(LocalDate.now().toString())
                    .horaExclusao(LocalTime.now().toString())
                    .build();

            cliente.setExclusao(exclusao);
            log.debug("Objeto ExclusaoCliente do cliente de id {} setado com sucesso", cliente.getId());
        }

        log.debug("Verificando se listagem de clientes encontrados está preenchida...");
        if (!clientesEncontrados.isEmpty()) {
            log.debug("Persistindo cliente excluído no banco de dados...");
            clienteRepositoryImpl.implementaPersistenciaEmMassa(clientesEncontrados);
        } else throw new InvalidRequestException("Nenhum cliente foi encontrado para remoção");

        log.info("Clientes excluídos com sucesso");
    }

    public void validaSeClienteEstaExcluido(ClienteEntity cliente, String mensagemCasoEstejaExcluido) {
        log.debug("Método de validação de cliente excluído acessado");
        if (cliente.getExclusao() != null) {
            log.debug("Cliente de id {}: Validação de cliente já excluído falhou. Não é possível realizar operações " +
                    "em um cliente que já foi excluído.", cliente.getId());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.debug("O cliente de id {} não está excluído", cliente.getId());
    }

    public ClienteResponse realizaBuscaDeClientePorId(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de obtenção de cliente por id. ID recebido: {}", id);

        log.debug("Acessando repositório de busca de cliente por ID...");
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());

        log.debug("Busca de clientes por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        ClienteResponse clienteResponse = clienteTypeConverter.converteClienteEntityParaClienteResponse(cliente);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca de cliente por id foi realizada com sucesso");
        return clienteResponse;
    }

    public ClienteResponse atualizaCliente(EmpresaEntity empresaLogada, Long id, ClienteRequest clienteRequest) {
        log.debug("Método de serviço de atualização de cliente acessado");

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());

        log.debug("Iniciando acesso ao método de validação de alteração de dados de cliente excluído...");
        validaSeClienteEstaExcluido(clienteEncontrado, "Não é possível atualizar um cliente excluído");

        log.debug("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaClienteAtualizado(clienteRequest, clienteEncontrado, empresaLogada);

        Set<PerfilEnum> acessoCliente = new HashSet<>();
        acessoCliente.add(PerfilEnum.CLIENTE);

        log.debug("Iniciando criação do objeto ClienteEntity...");
        ClienteEntity novoClienteAtualizado = ClienteEntity.builder()
                .idEmpresaResponsavel(clienteEncontrado.getIdEmpresaResponsavel())
                .id(clienteEncontrado.getId())
                .dataCadastro(clienteEncontrado.getDataCadastro())
                .horaCadastro(clienteEncontrado.getHoraCadastro())
                .nome(clienteRequest.getNome())
                .email(clienteRequest.getEmail())
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .statusCliente(clienteRequest.getStatusCliente())
                .dataNascimento(clienteRequest.getDataNascimento())
                .tipoPessoa(clienteRequest.getTipoPessoa())
                .acessoSistema(clienteRequest.getAcessoSistema() != null
                        ? AcessoSistemaEntity.builder()
                        .senha(realizaTratamentoSenha(clienteRequest, clienteEncontrado))
                        .perfis(acessoCliente)
                        .build()
                        : null)
                .exclusao(clienteEncontrado.getExclusao())
                .endereco(clienteRequest.getEndereco())
                .telefones(clienteRequest.getTelefones())
                .planos(clienteEncontrado.getPlanos())
                .cartoes(clienteEncontrado.getCartoes())
                .build();
        log.debug("Objeto cliente construído com sucesso");

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(novoClienteAtualizado);

        log.debug("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
        ClienteResponse clienteResponse = clienteTypeConverter.converteClienteEntityParaClienteResponse(clientePersistido);

        log.info("Cliente atualizado com sucesso");
        return clienteResponse;
    }

    private String realizaTratamentoSenha(ClienteRequest clienteRequest, ClienteEntity clienteEncontrado) {
        if (clienteEncontrado.getAcessoSistema() == null)
            return new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha());
        else {
            if (clienteEncontrado.getAcessoSistema().getSenha().equals(clienteRequest.getAcessoSistema().getSenha())) {
                return clienteEncontrado.getAcessoSistema().getSenha();
            } else {
                return new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha());
            }
        }
    }

}
