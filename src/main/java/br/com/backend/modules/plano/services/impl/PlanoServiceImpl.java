package br.com.backend.modules.plano.services.impl;

import br.com.backend.exceptions.custom.InternalErrorException;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.repository.impl.ClienteRepositoryImpl;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.models.dto.response.DadosPlanoResponse;
import br.com.backend.modules.plano.models.dto.response.page.PlanoPageResponse;
import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import br.com.backend.modules.plano.proxy.operations.atualizacao.impl.AtualizacaoPlanoAsaasProxyImpl;
import br.com.backend.modules.plano.proxy.operations.criacao.CriacaoPlanoAsaasProxyImpl;
import br.com.backend.modules.plano.proxy.operations.remocao.impl.RemocaoPlanoAsaasProxyImpl;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.plano.repository.impl.PlanoRepositoryImpl;
import br.com.backend.modules.plano.services.PlanoService;
import br.com.backend.modules.plano.services.utils.PlanoUtils;
import br.com.backend.modules.plano.services.validator.PlanoValidator;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PlanoServiceImpl implements PlanoService {

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Autowired
    CriacaoPlanoAsaasProxyImpl criacaoPlanoAsaasProxy;

    @Autowired
    AtualizacaoPlanoAsaasProxyImpl atualizacaoPlanoAsaasProxy;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxy;

    @Autowired
    PlanoValidator planoValidator;

    @Autowired
    PlanoUtils planoUtils;

    @Override
    public PlanoResponse criaNovoPlano(UUID uuidEmpresaSessao,
                                       UUID uuidCliente,
                                       PlanoRequest planoRequest) {

        log.info("Método de serviço de criação de novo plano acessado");

        log.info("Iniciando acesso ao método de validação de plano...");
        planoValidator.realizaValidacaoCriacaoNovoPlano(planoRequest);

        log.info("Iniciando acesso ao método de busca de cliente por id com o id {}...", uuidCliente);
        ClienteEntity clienteEncontrado = clienteRepositoryImpl
                .implementaBuscaPorUUID(new ClienteId(uuidEmpresaSessao, uuidCliente));

        log.info("Obtendo objeto da empresa da sessão através do cliente encontrado...");
        EmpresaEntity empresaSessao = clienteEncontrado.getEmpresa();

        log.info("Iniciando acesso ao método de implementação da criação do cliente na integradora ASAAS...");
        String idPlanoAsaas = criacaoPlanoAsaasProxy
                .realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoRequest, clienteEncontrado.getAsaasId());
        log.info("Criação do cliente na integradora ASAAS realizada com sucesso");


        try {
            log.info("Iniciando criação do objeto PlanoEntity...");
            PlanoEntity planoEntity = new PlanoEntity()
                    .constroiPlanoEntityParaCriacao(empresaSessao, idPlanoAsaas, clienteEncontrado, planoRequest);
            log.info("Objeto planoEntity criado com sucesso");

            log.info("Acoplando plano ao cliente encontrado...");
            clienteEncontrado.addPlano(planoEntity);

            log.info("Iniciando acesso ao método de implementação da persistência do cliente...");
            PlanoEntity planoPersistido = (clienteRepositoryImpl
                    .implementaPersistencia(clienteEncontrado)).obtemUltimoPlanoPersistido();
            log.info("Persistência do cliente com plano acoplado realizada com sucesso!");

            log.info("Convertendo planoEntity criado para planoResponse...");
            PlanoResponse planoResponse = new PlanoResponse()
                    .constroiPlanoResponse(planoPersistido);

            log.info("Plano criado com sucesso");
            return planoResponse;
        } catch (Exception e) {
            log.error("Ocorreu um erro durante o processo de persistência do plano: {}", e.getMessage());

            log.info("Iniciando acesso ao método de cancelamento do plano na integradora ASAAS " +
                    "para realização de rollback...");
            remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(idPlanoAsaas);

            log.info("Rollback do plano na integradora ASAAS finalizado com sucesso");
            throw new InternalErrorException("Ocorreu um erro durante a tentativa de persistência do plano. " +
                    "Erro: " + e.getMessage());
        }

    }

    @Override
    public PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(Pageable pageable,
                                                                    UUID uuidEmpresaSessao,
                                                                    UUID uuidCliente) {
        log.info("Método de serviço de obtenção paginada de planos do cliente acessado");

        log.info("Acessando repositório de busca de planos do cliente");
        Page<PlanoEntity> planoPage = planoRepository.buscaPaginadaPorPlanosDoCliente(pageable, uuidEmpresaSessao, uuidCliente);

        log.info("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = new PlanoPageResponse().constroiPlanoPageResponse(planoPage);
        log.info(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    @Override
    public PlanoPageResponse realizaBuscaPaginadaPorPlanos(Pageable pageable,
                                                           UUID uuidEmpresaSessao,
                                                           String campoBusca) {
        log.info("Método de serviço de obtenção paginada de planos acessado. Campo de busca: {}", campoBusca);

        log.info("Acessando repositório de busca de planos");
        Page<PlanoEntity> planoPage = planoRepository.buscaPaginadaPorPlanos(pageable, uuidEmpresaSessao, campoBusca);

        log.info("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = new PlanoPageResponse().constroiPlanoPageResponse(planoPage);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    @Override
    public PlanoResponse realizaBuscaDePlanoPorId(UUID uuidEmpresaSessao,
                                                  UUID uuidPlano) {
        log.info("Método de serviço de obtenção de plano por id acessado");

        log.info("Acessando repositório de busca de plano por ID...");
        PlanoEntity plano = planoRepositoryImpl.implementaBuscaPorId(new PlanoId(uuidEmpresaSessao, uuidPlano));

        log.info("Busca de plano por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        PlanoResponse planoResponse = new PlanoResponse().constroiPlanoResponse(plano);

        log.info("A busca de plano por id foi realizada com sucesso");
        return planoResponse;
    }

    @Override
    public DadosPlanoResponse realizaBuscaDeDadosDePlanoPorId(UUID uuidEmpresaSessao,
                                                              UUID uuidPlano) {

        log.info("Método de serviço de obtenção de dados de um plano por id acessado");

        Integer quantidadeDeCobrancas = pagamentoRepository
                .somaQuantidadeDeCobrancasDoPlano(uuidPlano);

        Integer quantidadeDeCobrancasAtrasadas = pagamentoRepository
                .somaQuantidadeDeCobrancasAtrasadasDoPlano(uuidPlano);

        Double comprometimento = quantidadeDeCobrancas > 0
                ? 100 - ((quantidadeDeCobrancasAtrasadas * 100.0) / (quantidadeDeCobrancas))
                : 100.0;

        log.info("Iniciando construção do objeto DadosPlanoResponse...");
        DadosPlanoResponse dadosPlanoResponse = DadosPlanoResponse.builder()
                .totalCobrancas(pagamentoRepository
                        .calculaTotalCobrancasDoPlano(uuidPlano))
                .totalPendente(pagamentoRepository
                        .calculaTotalCobrancasPendentesDoPlano(uuidPlano))
                .totalPago(pagamentoRepository
                        .calculaTotalCobrancasAprovadasDoPlano(uuidPlano))
                .totalEmAtraso(pagamentoRepository
                        .calculaTotalCobrancasAtrasadasDoPlano(uuidPlano))
                .comprometimento(comprometimento)
                .build();

        log.info("A busca dos dados do plano foi realizada com sucesso");
        return dadosPlanoResponse;
    }

    @Override
    public PlanoResponse atualizaPlano(UUID uuidEmpresaSessao,
                                       UUID uuidPlano,
                                       PlanoRequest planoRequest) {
        log.info("Método de serviço de atualização de plano acessado");

        log.info(ConstantesPlano.BUSCA_PLANO_POR_ID);
        PlanoEntity planoEncontrado = planoRepositoryImpl
                .implementaBuscaPorId(new PlanoId(uuidEmpresaSessao, uuidPlano));

        log.info("Iniciando acesso ao método de validação de alteração de dados de plano excluído...");
        planoUtils.validaSePlanoEstaExcluido(planoEncontrado,
                "Não é possível atualizar um plano excluído");

        log.info("Iniciando atualização do objeto PlanoEntity...");
        PlanoEntity planoAtualizado = new PlanoEntity()
                .atualizaEntidadeComAtributosRequest(planoEncontrado, planoRequest);

        log.info("Iniciando criação do objeto PlanoEntity...");

        log.info("Objeto plano construído com sucesso");

        log.info("Iniciando acesso ao método de implementação da persistência do plano...");
        PlanoEntity planoPersistido = planoRepositoryImpl.implementaPersistencia(planoAtualizado);

        log.info("Iniciando processo de atualização do plano na integradora ASAAS...");
        atualizacaoPlanoAsaasProxy.realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoAtualizado);

        log.info("Plano persistido com sucesso. Convertendo PlanoEntity para PlanoResponse...");
        PlanoResponse planoResponse = new PlanoResponse().constroiPlanoResponse(planoPersistido);

        log.info("Plano atualizado com sucesso");
        return planoResponse;
    }

    @Override
    public PlanoResponse cancelaAssinatura(UUID uuidEmpresaSessao,
                                           UUID uuidPlano) {

        log.info("Método responsável por realizar o cancelamento de uma assinatura acessado");
        PlanoEntity planoEncontrado = planoRepositoryImpl
                .implementaBuscaPorId(new PlanoId(uuidEmpresaSessao, uuidPlano));

        log.info("Verificando se o plano de assinatura já foi removido anteriormente...");
        if (planoEncontrado.getStatusPlano().equals(StatusPlanoEnum.REMOVIDO))
            throw new InvalidRequestException("Não é possível remover um plano que já foi removido");

        log.info("Setando status do plano como removido...");
        planoEncontrado.setStatusPlano(StatusPlanoEnum.REMOVIDO);

        log.info("Iniciando acesso ao método de implementação da persistência do plano removido...");
        PlanoEntity planoRemovido = planoRepositoryImpl.implementaPersistencia(planoEncontrado);

        log.info("Iniciando acesso ao método de implementação de cancelamento de assinatura na integradora ASAAS...");
        remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(planoEncontrado.getAsaasId());

        log.info("Convertendo planoEntity criado para planoResponse...");
        PlanoResponse planoResponse = new PlanoResponse().constroiPlanoResponse(planoRemovido);

        log.info("Plano removido com sucesso");
        return planoResponse;
    }

    @Override
    public void removePlanosDoCliente(UUID uuidCliente) {
        log.info("Método responsável por realizar a remoção de todos os planos vinculados ao cliente de uuid {} " +
                "acessado", uuidCliente);

        log.info("Iniciando busca por planos do cliente...");
        List<PlanoEntity> planos = planoRepository.buscaPorPlanosDoCliente(uuidCliente);
        log.info("Planos do cliente buscados com sucesso");

        log.info("Iniciando acesso à iteração pelos planos encontrados para atualização de status...");
        planos.forEach(plano -> {
                    log.info("Iteração pelo plano de id {} em andamento...", plano.getUuid());
                    switch (plano.getStatusPlano()) {
                        case ATIVO -> {
                            log.info("O plano iterado possui status ATIVO");

                            log.info("Realizando setagem de data de agendamento para remoção do plano...");
                            plano.setDataAgendamentoRemocao(LocalDate.parse(
                                    plano.getDataVencimento()).plusDays(1L).toString());
                            log.info("Setagem de data de agendamento para remoção realizada com sucesso");
                        }
                        case INATIVO -> {
                            log.info("O plano iterado possui status INATIVO");

                            log.info("Iniciando setagem do plano como REMOVIDO...");
                            plano.setStatusPlano(StatusPlanoEnum.REMOVIDO);

                            log.info("Iniciando acesso ao método de implementação da remoção de assinatura na integradora ASAAS...");
                            remocaoPlanoAsaasProxy.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(plano.getAsaasId());
                        }
                        case REMOVIDO -> {
                            log.info("O plano iterado possui status REMOVIDO");
                            log.warn("Não foi possível realizar a remoção em um plano que já está removido");
                        }
                    }
                }
        );

    }

}
