package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import br.com.backend.proxy.AsaasProxy;
import br.com.backend.proxy.plano.request.CriaPlanoAsaasRequest;
import br.com.backend.proxy.plano.request.split.SplitRequest;
import br.com.backend.proxy.plano.response.CriaPlanoAsaasResponse;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.repositories.plano.PlanoRepository;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Slf4j
@Service
public class PlanoService {

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    @Autowired
    AsaasProxy asaasProxy;

    public PlanoResponse criaNovoPlano(EmpresaEntity empresaLogada, Long idCliente, PlanoRequest planoRequest) {

        log.debug("Método de serviço de criação de novo plano acessado");

        log.debug("Iniciando acesso ao método de busca de cliente por id com o id {} na empresa de id {}...",
                idCliente, empresaLogada.getId());
        ClienteEntity clienteEntity = clienteRepositoryImpl.implementaBuscaPorId(idCliente, empresaLogada.getId());

        log.debug("Iniciando criação do objeto PlanoEntity...");
        PlanoEntity planoEntity = PlanoEntity.builder()
                .idEmpresaResponsavel(empresaLogada.getId())
                .idClienteResponsavel(idCliente)
                .idAsaas((planoRequest.getFormaPagamento() == FormaPagamentoEnum.PIX || planoRequest.getFormaPagamento() == FormaPagamentoEnum.BOLETO)
                        ? realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoRequest, clienteEntity, empresaLogada)
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
        clienteEntity.getPlanos().add(planoEntity);

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente com o novo plano acoplado...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEntity);

        log.debug("Iniciando obtenção do plano criado...");
        PlanoEntity planoPersistido = clientePersistido.getPlanos().get(clientePersistido.getPlanos().size() - 1);

        log.debug("Convertendo planoEntity criado para planoResponse...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoPersistido);

        log.info("Plano criado com sucesso");
        return planoResponse;
    }

    private String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
                                                                       ClienteEntity clienteEntity,
                                                                       EmpresaEntity empresaEntity) {


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
                .split(SplitRequest.builder()
                        .walletId(empresaEntity.getContaEmpresaAsaas().getWalletId())
                        .fixedValue(calculaRepasseSplitPlano(planoRequest))
                        .percentualValue(null)
                        .build())
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
            throw new InvalidRequestException("O retorno da integradora é nulo");
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
            throw new InvalidRequestException("O retorno da integradora é nulo");
        }

        log.debug("Retornando id da assinatura gerado: {}", planoAsaasResponse.getId());
        return planoAsaasResponse.getId();
    }

    private Double calculaRepasseSplitPlano(PlanoRequest planoRequest) {

        double taxaSistema = 2.0;
        double taxaIntegradora;
        Double valorPlano = planoRequest.getValor();

        switch (planoRequest.getFormaPagamento()) {
            case PIX:
            case BOLETO: {
                taxaIntegradora = 1.99;
                break;
            }
            default: {
                taxaIntegradora = 0.49 + ((valorPlano / 100) * 1.99);
            }
        }

        return (valorPlano - (taxaIntegradora + taxaSistema));

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
        log.debug("Conversão de tipagem realizada com sucesso");

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
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }
}
