package br.com.backend.modules.plano.proxy.operations.atualizacao.impl;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.proxy.PlanoAsaasProxy;
import br.com.backend.modules.plano.proxy.operations.atualizacao.models.request.AtualizaAssinaturaAsaasRequest;
import br.com.backend.modules.plano.proxy.operations.atualizacao.models.response.AtualizaAssinaturaAsaasResponse;
import br.com.backend.modules.plano.proxy.utils.PlanoAsaasUtil;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizacaoPlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    @Autowired
    PlanoAsaasUtil planoAsaasUtil;

    public void realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoEntity planoAtualizado) {

        log.debug("Método de serviço responsável pela atualização de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto AtualizaAssinaturaAsaasRequest...");
        AtualizaAssinaturaAsaasRequest atualizaAssinaturaAsaasRequest = AtualizaAssinaturaAsaasRequest.builder()
                .billingType(planoAtualizado.getFormaPagamento())
                .value(planoAtualizado.getValor())
                .nextDueDate(planoAtualizado.getDataInicio())
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(planoAsaasUtil.transformaPeriodicidadeEnumEmCycleEnum(planoAtualizado.getPeriodicidade()))
                .description(planoAtualizado.getDescricao())
                .updatePendingPayments(true)
                .externalReference(null)
                .build();

        ResponseEntity<AtualizaAssinaturaAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de atualização de assinatura para a integradora ASAAS...");
            responseAsaas =
                    planoAsaasProxy.atualizaAssinatura(
                            planoAtualizado.getAsaasId(),
                            atualizaAssinaturaAsaasRequest,
                            System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesPlano.ERRO_ATUALIZACAO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesPlano.ERRO_ATUALIZACAO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na atualização da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de atualização da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesPlano.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Atualização de assinatura ASAAS realizada com sucesso");

        AtualizaAssinaturaAsaasResponse planoAsaasResponse = responseAsaas.getBody();

        if (planoAsaasResponse == null) {
            log.error("O valor retornado pela integradora na atualização da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }
}
