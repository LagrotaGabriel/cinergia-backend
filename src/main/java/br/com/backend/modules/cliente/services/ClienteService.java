package br.com.backend.modules.cliente.services;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.exceptions.custom.UnauthorizedAccessException;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.cliente.models.dto.response.page.ClientePageResponse;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.proxy.impl.atualizacao.AtualizacaoClienteAsaasProxyImpl;
import br.com.backend.modules.cliente.proxy.impl.criacao.CriacaoClienteAsaasProxyImpl;
import br.com.backend.modules.cliente.proxy.impl.remocao.RemocaoClienteAsaasProxyImpl;
import br.com.backend.modules.cliente.repository.ClienteRepository;
import br.com.backend.modules.cliente.repository.impl.ClienteRepositoryImpl;
import br.com.backend.modules.cliente.services.utils.ClienteUtils;
import br.com.backend.modules.cliente.services.validator.ClienteValidationService;
import br.com.backend.modules.cliente.utils.ConstantesClientes;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ClienteService {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    ClienteValidationService clienteValidationService;

    @Autowired
    CriacaoClienteAsaasProxyImpl criacaoClienteAsaasProxyImpl;

    @Autowired
    AtualizacaoClienteAsaasProxyImpl atualizacaoClienteAsaasProxy;

    @Autowired
    RemocaoClienteAsaasProxyImpl remocaoClienteAsaasProxyImpl;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    EmpresaRepository empresaRepository;

    @Transactional
    public ClienteResponse criaNovoCliente(UUID uuidEmpresaSessao,
                                           ClienteRequest clienteRequest) {

        log.info("Método de serviço de criação de novo cliente acessado");

        log.info("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaNovoCliente(uuidEmpresaSessao, clienteRequest);

        log.info("Realizando a busca da empresa da sessão atual pelo id do principal do token: {}", uuidEmpresaSessao);
        EmpresaEntity empresaLogada = empresaRepository.findById(uuidEmpresaSessao)
                .orElseThrow(() -> {
                    log.error("Nenhuma empresa foi encontrada através do id informado pelo token: {}", uuidEmpresaSessao);
                    return new UnauthorizedAccessException("O token enviado é inválido");
                });
        log.info("Busca da empresa realizada com sucesso");

        log.info("Iniciando acesso ao método de implementação da criação do cliente na integradora ASAAS...");
        String idClienteAsaas = activeProfile.equals("dev")
                ? "AMBIENTE DE DESENVOLVIMENTO_" + LocalDateTime.now()
                : criacaoClienteAsaasProxyImpl.realizaCriacaoClienteAsaas(uuidEmpresaSessao, clienteRequest);
        log.info("Criação do cliente na integradora ASAAS realizada com sucesso");

        log.info("Iniciando criação do objeto ClienteEntity...");
        ClienteEntity clienteEntity = new ClienteEntity()
                .constroiClienteEntityParaCriacao(empresaLogada, idClienteAsaas, clienteRequest);
        log.info("Objeto clienteEntity criado com sucesso");

        try {
            log.info("Iniciando acesso ao método de implementação da persistência do cliente...");
            ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEntity);

            log.info("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
            return new ClienteResponse().constroiClienteResponse(clientePersistido);
        } catch (Exception e) {
            //TODO CRIAR EXCEPTION INTERNAL ERROR E AJUSTAR MENSAGEM DE ERRO

            //TODO CRIAR MÉTODO PARA REALIZAR "ROLLBACK" NA ASAAS
            throw new InvalidRequestException("DEU RUIM!!!!");
        }
    }

    public ClientePageResponse realizaBuscaPaginadaPorClientes(UUID uuidEmpresaSessao,
                                                               Pageable pageable,
                                                               String campoBusca) {
        log.info("Método de serviço de obtenção paginada de clientes acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.info("Acessando repositório de busca de clientes");
        Page<ClienteEntity> clientePage = campoBusca != null && !campoBusca.isEmpty()
                ? clienteRepository.buscaPorClientesTypeAhead(pageable, uuidEmpresaSessao, campoBusca)
                : clienteRepository.buscaPorClientes(pageable, uuidEmpresaSessao);

        log.info("Busca de clientes por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        ClientePageResponse clientePageResponse =
                new ClientePageResponse().constroiClientePageResponse(clientePage);
        log.info("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de clientes foi realizada com sucesso");
        return clientePageResponse;
    }

    public ClienteResponse realizaBuscaDeClientePorId(ClienteId clienteId) {
        log.info("Método de serviço de obtenção de cliente por uuid. ID recebido: {}", clienteId);

        log.info("Acessando repositório de busca de cliente por ID...");
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorUUID(clienteId);

        log.info("Busca de clientes por uuid realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        ClienteResponse clienteResponse = new ClienteResponse()
                .constroiClienteResponse(cliente);

        log.info("A busca de cliente por uuid foi realizada com sucesso");
        return clienteResponse;
    }

    public byte[] obtemImagemPerfilCliente(ClienteId clienteId) {
        return clienteRepositoryImpl.implementaBuscaDeImagemDePerfilPorId(clienteId).getArquivo();
    }

    @Transactional
    public ClienteResponse atualizaCliente(ClienteId clienteId,
                                           ClienteRequest clienteRequest) {
        log.info("Método de serviço de atualização de cliente acessado");

        log.info(ConstantesClientes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorUUID(clienteId);

        log.info("Iniciando acesso ao método de validação de alteração de dados de cliente excluído...");
        ClienteUtils.validaSeClienteEstaExcluido(clienteEncontrado,
                "Não é possível atualizar um cliente excluído");

        log.info("Iniciando acesso ao método de validação de chave única...");
        clienteValidationService.validaSeChavesUnicasJaExistemParaClienteAtualizado(
                clienteId.getEmpresa(), clienteRequest, clienteEncontrado);

        log.info("Iniciando criação do objeto ClienteEntity...");
        clienteEncontrado.atualizaEntidadeComAtributosRequest(clienteRequest);

        log.info("Iniciando acesso ao método de implementação da persistência do cliente...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.info("Iniciando processo de atualização do cliente na integradora ASAAS...");
        if (!activeProfile.equals("dev"))
            atualizacaoClienteAsaasProxy.realizaAtualizacaoClienteAsaas(
                    clienteId.getEmpresa(), clienteEncontrado.getAsaasId(), clienteRequest);

        log.info("Cliente persistido com sucesso. Convertendo clienteEntity para clienteResponse...");
        ClienteResponse clienteResponse = new ClienteResponse().constroiClienteResponse(clientePersistido);

        log.info("Cliente atualizado com sucesso");
        return clienteResponse;
    }

    @Transactional
    public ClienteResponse atualizaImagemPerfilCliente(ClienteId clienteId,
                                                       MultipartFile fotoPerfil) throws IOException {

        log.info("Método de serviço de atualização de foto de perfil de cliente acessado");

        log.info("Iniciando construção do objeto ArquivoEntity da foto de perfil do cliente...");
        ImagemEntity fotoPerfilEntity = new ImagemEntity().constroiImagemEntity(fotoPerfil);

        log.info("Acessando repositório de busca de cliente por ID...");
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorUUID(clienteId);

        log.info("Acoplando foto de perfil ao objeto do cliente...");
        cliente.setFotoPerfil(fotoPerfilEntity);

        log.info(ConstantesClientes.INICIANDO_IMPL_PERSISTENCIA_CLIENTE);
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(cliente);

        return new ClienteResponse().constroiClienteResponse(clientePersistido);
    }

    @Transactional
    public ClienteResponse removeCliente(ClienteId clienteId) {
        log.info("Método de serviço de remoção de cliente acessado");

        log.info(ConstantesClientes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorUUID(clienteId);

        log.info("Iniciando acesso ao método de validação de exclusão de cliente que já foi excluído...");
        ClienteUtils.validaSeClienteEstaExcluido(clienteEncontrado,
                "O cliente selecionado já foi excluído");

        log.info("Atualizando objeto ExclusaoCliente do cliente com dados referentes à sua exclusão...");
        clienteEncontrado.criaExclusao();

        log.info("Persistindo cliente excluído no banco de dados...");
        ClienteEntity clienteExcluido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.info("Iniciando acesso ao método de remoção de cliente na integradora ASAAS...");
        if (!activeProfile.equals("dev"))
            remocaoClienteAsaasProxyImpl.realizaRemocaoDoClienteNaIntegradoraAsaas(clienteExcluido);

        log.info("Iniciando iteração pelos planos do cliente para acionar cancelamento dos planos...");
        clienteExcluido.getPlanos().forEach(planoEntity -> {
            //TODO CRIAR ITERAÇÃO NA CLASSE DE SERVIÇO DO PLANO PARA REMOVER TODOS OS PLANOS DO CLIENTE
        });

        log.info("Cliente excluído com sucesso");
        return new ClienteResponse().constroiClienteResponse(clienteExcluido);
    }
}
