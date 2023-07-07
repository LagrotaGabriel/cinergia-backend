package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.dto.plano.response.DadosPlanoResponse;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import br.com.backend.proxy.AsaasProxy;
import br.com.backend.proxy.plano.request.CriaPlanoAsaasRequest;
import br.com.backend.proxy.plano.response.CriaPlanoAsaasResponse;
import br.com.backend.proxy.plano.response.cancela.CancelamentoAssinaturaResponse;
import br.com.backend.proxy.plano.response.consulta.ConsultaAssinaturaResponse;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.repositories.plano.PlanoRepository;
import br.com.backend.repositories.plano.impl.PlanoRepositoryImpl;
import br.com.backend.services.exceptions.FeignConnectionException;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlanoService {

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    @Autowired
    AsaasProxy asaasProxy;

    @Transactional
    public PlanoResponse criaNovoPlano(EmpresaEntity empresaLogada, Long idCliente, PlanoRequest planoRequest) {

        log.debug("Método de serviço de criação de novo plano acessado");

        log.debug("Iniciando acesso ao método de busca de cliente por id com o id {} na empresa de id {}...",
                idCliente, empresaLogada.getId());
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(idCliente, empresaLogada.getId());

        log.debug("Iniciando criação do objeto PlanoEntity...");
        PlanoEntity planoEntity = PlanoEntity.builder()
                .idEmpresaResponsavel(empresaLogada.getId())
                .idClienteResponsavel(idCliente)
                .idAsaas((planoRequest.getFormaPagamento() == FormaPagamentoEnum.PIX || planoRequest.getFormaPagamento() == FormaPagamentoEnum.BOLETO)
                        ? realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoRequest, clienteEncontrado)
                        : null)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataInicio(planoRequest.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(StatusPlanoEnum.INATIVO)
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .cartao(null)
                .pagamentos(new ArrayList<>())
                .build();
        log.debug("Objeto planoEntity criado com sucesso");

        log.debug("Acoplando plano ao cliente encontrado...");
        clienteEncontrado.getPlanos().add(planoEntity);

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente com o novo plano acoplado...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.debug("Atualização do saldo da empresa logada realizada com sucesso");

        log.debug("Iniciando obtenção do plano criado...");
        PlanoEntity planoPersistido = clientePersistido.getPlanos().get(clientePersistido.getPlanos().size() - 1);

        log.debug("Convertendo planoEntity criado para planoResponse...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoPersistido);

        log.info("Plano criado com sucesso");
        return planoResponse;
    }

    private String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
                                                                       ClienteEntity clienteEntity) {

        log.debug("Método de serviço responsável pela criação de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto CriaPlanoAsaasRequest...");
        CriaPlanoAsaasRequest criaPlanoAsaasRequest = CriaPlanoAsaasRequest.builder()
                .customer(clienteEntity.getAsaasId())
                .billingType(planoRequest.getFormaPagamento())
                .value(planoRequest.getValor())
                .nextDueDate(planoRequest.getDataInicio())
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(planoTypeConverter.transformaPeriodicidadeEnumEmCycleEnum(planoRequest.getPeriodicidade()))
                .description(planoRequest.getDescricao())
                .endDate(null)
                .maxPayments(null)
                .externalReference(null)
                .split(null)
                .build();

        ResponseEntity<CriaPlanoAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de criação de assinatura para a integradora ASAAS...");
            responseAsaas =
                    asaasProxy.cadastraNovaAssinatura(criaPlanoAsaasRequest, System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Criação de assinatura ASAAS realizada com sucesso");

        CriaPlanoAsaasResponse planoAsaasResponse = responseAsaas.getBody();

        if (planoAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.debug("Retornando id da assinatura gerado: {}", planoAsaasResponse.getId());
        return planoAsaasResponse.getId();
    }

    @Transactional
    public PlanoResponse cancelaAssinatura(Long idAssinatura, EmpresaEntity empresa) {

        log.debug("Método responsável por realizar o cancelamento de uma assinatura acessado. ID: {}", idAssinatura);
        PlanoEntity plano = planoRepositoryImpl.implementaBuscaPorId(idAssinatura, empresa.getId());

        log.debug("Iniciando acesso ao método de cancelamento de assinatura na integradora ASAAS...");
        realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(plano);

        log.debug("Setando status do plano como removido...");
        plano.setStatusPlano(StatusPlanoEnum.REMOVIDO);

        log.debug("Implementando persistência do plano de assinatura atualizado...");
        PlanoResponse planoResponse =
                planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoRepositoryImpl.implementaPersistencia(plano));

        log.info("Cancelamento da assinatura realizado com sucesso");
        return planoResponse;
    }

    private void realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoEntity planoEntity) {

        log.debug("Método de serviço responsável pela cancelamento de assinatura na integradora ASAAS acessado");
        ResponseEntity<CancelamentoAssinaturaResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de assinatura para a integradora ASAAS...");
            responseAsaas =
                    asaasProxy.cancelarAssinatura(planoEntity.getId(), System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de cancelamento da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Cancelamento de assinatura ASAAS realizada com sucesso");

        CancelamentoAssinaturaResponse cancelamentoAssinaturaResponse = responseAsaas.getBody();

        if (cancelamentoAssinaturaResponse == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(EmpresaEntity empresaLogada,
                                                                    Pageable pageable,
                                                                    Long idCliente) {
        log.debug("Método de serviço de obtenção paginada de planos do cliente acessado");

        log.debug("Acessando repositório de busca de planos do cliente");
        Page<PlanoEntity> planoPage = planoRepository.buscaPorPlanosDoCliente(pageable, empresaLogada.getId(), idCliente);

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanos(EmpresaEntity empresaLogada,
                                                           Pageable pageable,
                                                           String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de planos acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de planos");
        Page<PlanoEntity> planoPage = campoBusca != null && !campoBusca.isEmpty()
                ? planoRepository.buscaPorPlanosTypeAhead(pageable, campoBusca, empresaLogada.getId())
                : planoRepository.buscaPorPlanos(pageable, empresaLogada.getId());

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    public PlanoResponse realizaBuscaDePlanoPorId(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de obtenção de plano por id. ID recebido: {}", id);

        log.debug("Acessando repositório de busca de plano por ID...");
        PlanoEntity plano = planoRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());

        log.debug("Busca de plano por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(plano);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca de plano por id foi realizada com sucesso");
        return planoResponse;
    }

    public DadosPlanoResponse realizaBuscaDeDadosDePlanoPorId(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de obtenção de dados de um plano por id. ID recebido: {}", id);

        log.debug("Acessando repositório de busca de pagamentos de um plano por ID...");
        List<PagamentoEntity> pagamentos = planoRepositoryImpl.implementaBuscaDePagamentosDoPlanoPorId(id, empresaLogada.getId());

        log.debug("Busca de pagamentos do plano por id realizada com sucesso. Acessando método de cálculo de dados...");
        DadosPlanoResponse dadosPlanoResponse = calculaDadosDoPlano(pagamentos);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca de plano por id foi realizada com sucesso");
        return dadosPlanoResponse;
    }

    private DadosPlanoResponse calculaDadosDoPlano(List<PagamentoEntity> pagamentos) {

        log.debug("Método responsável por calcular os dados de pagamentos do plano acessado");

        log.debug("Iniciando criação das variáveis...");
        double quantidadeCobrancas = 0.0;
        Double totalCobrancas = 0.0;

        Double totalPendente = 0.0;

        Double totalPago = 0.0;

        double quantidadeEmAtraso = 0.0;
        Double totalEmAtraso = 0.0;

        double comprometimento = 100.0;

        for (PagamentoEntity pagamento : pagamentos) {
            quantidadeCobrancas++;
            totalCobrancas += pagamento.getValorBruto();

            if (pagamento.getStatusPagamento() == StatusPagamentoEnum.APROVADO) {
                totalPago += pagamento.getValorBruto();
            } else if (pagamento.getStatusPagamento() == StatusPagamentoEnum.PENDENTE) {
                totalPendente += pagamento.getValorBruto();
            } else {
                quantidadeEmAtraso++;
                totalEmAtraso += pagamento.getValorBruto();
            }

        }

        if (quantidadeCobrancas > 0.0) comprometimento -= ((quantidadeEmAtraso * 100) / (quantidadeCobrancas));

        log.debug("Iniciando construção do objeto DadosPlanoResponse...");
        DadosPlanoResponse dadosPlanoResponse = DadosPlanoResponse.builder()
                .totalCobrancas(totalCobrancas)
                .totalPendente(totalPendente)
                .totalPago(totalPago)
                .totalEmAtraso(totalEmAtraso)
                .comprometimento(comprometimento)
                .build();

        log.debug("Objeto construído com sucesso. Retornando objeto...");
        return dadosPlanoResponse;
    }

    public ConsultaAssinaturaResponse consultaAssinaturaAsaas(String id) {
        log.debug("Método de consulta de plano ASAAS por ID ({}) acessado", id);
        ResponseEntity<ConsultaAssinaturaResponse> assinaturaAsaas;
        try {
            assinaturaAsaas =
                    asaasProxy.consultaAssinatura(id, System.getenv(Constantes.TOKEN_ASAAS));
            log.debug("Plano encontrado: {}", assinaturaAsaas);
        } catch (Exception e) {
            log.error("Houve uma falha de comunicação com a integradora de pagamentos: {}", e.getMessage());
            throw new FeignConnectionException(Constantes.FALHA_COMUNICACAO_ASAAS + e.getMessage());
        }

        if (assinaturaAsaas.getStatusCodeValue() != 200) {
            log.error("Houve uma falha no processo de consulta do plano com a integradora de pagamentos: {}",
                    assinaturaAsaas.getBody());
            throw new InvalidRequestException("Ocorreu um erro no processo de consulta de plano com a integradora: "
                    + assinaturaAsaas.getBody());
        }

        log.debug("Retornando plano...");
        return assinaturaAsaas.getBody();
    }
}
