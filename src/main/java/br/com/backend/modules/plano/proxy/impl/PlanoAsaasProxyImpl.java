package br.com.backend.modules.plano.proxy.impl;

import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.proxy.PlanoAsaasProxy;
import br.com.backend.modules.plano.services.adapter.PlanoTypeConverter;
import br.com.backend.modules.plano.proxy.models.request.atualizar.AtualizaAssinaturaAsaasRequest;
import br.com.backend.modules.plano.proxy.models.request.criar.CriaPlanoAsaasRequest;
import br.com.backend.modules.plano.proxy.models.response.atualizar.AtualizaAssinaturaAsaasResponse;
import br.com.backend.modules.plano.proxy.models.response.cancelar.CancelamentoAssinaturaResponse;
import br.com.backend.modules.plano.proxy.models.response.consultar.ConsultaAssinaturaResponse;
import br.com.backend.modules.plano.proxy.models.response.criar.CriaPlanoAsaasResponse;
import br.com.backend.exceptions.FeignConnectionException;
import br.com.backend.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    public String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
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
                    planoAsaasProxy.cadastraNovaAssinatura(criaPlanoAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
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

    public void realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(String idPlanoAsaas,
                                                                             PlanoEntity planoEntity) {

        log.debug("Método de serviço responsável pela atualização de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto AtualizaAssinaturaAsaasRequest...");
        AtualizaAssinaturaAsaasRequest atualizaAssinaturaAsaasRequest = AtualizaAssinaturaAsaasRequest.builder()
                .billingType(planoEntity.getFormaPagamento())
                .value(planoEntity.getValor())
                .nextDueDate(planoEntity.getDataInicio())
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(planoTypeConverter.transformaPeriodicidadeEnumEmCycleEnum(planoEntity.getPeriodicidade()))
                .description(planoEntity.getDescricao())
                .updatePendingPayments(true)
                .externalReference(null)
                .build();

        ResponseEntity<AtualizaAssinaturaAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de atualização de assinatura para a integradora ASAAS...");
            responseAsaas =
                    planoAsaasProxy.atualizaAssinatura(idPlanoAsaas, atualizaAssinaturaAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(Constantes.ERRO_ATUALIZACAO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_ATUALIZACAO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na atualização da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de atualização da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Atualização de assinatura ASAAS realizada com sucesso");

        AtualizaAssinaturaAsaasResponse planoAsaasResponse = responseAsaas.getBody();

        if (planoAsaasResponse == null) {
            log.error("O valor retornado pela integradora na atualização da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

    public void realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoEntity planoEntity) {

        log.debug("Método de serviço responsável pela cancelamento de assinatura na integradora ASAAS acessado");
        ResponseEntity<CancelamentoAssinaturaResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de assinatura para a integradora ASAAS...");
            responseAsaas =
                    planoAsaasProxy.cancelarAssinatura(planoEntity.getIdAsaas(), System.getenv(Constantes.TOKEN_ASAAS));
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

    public ConsultaAssinaturaResponse consultaAssinaturaAsaas(String id) {
        log.debug("Método de consulta de plano ASAAS por ID ({}) acessado", id);
        ResponseEntity<ConsultaAssinaturaResponse> assinaturaAsaas;
        try {
            assinaturaAsaas =
                    planoAsaasProxy.consultaAssinatura(id, System.getenv(Constantes.TOKEN_ASAAS));
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
