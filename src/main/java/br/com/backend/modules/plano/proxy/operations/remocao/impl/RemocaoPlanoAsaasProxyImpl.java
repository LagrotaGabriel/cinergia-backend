package br.com.backend.modules.plano.proxy.operations.remocao.impl;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.proxy.PlanoAsaasProxy;
import br.com.backend.modules.plano.proxy.operations.remocao.response.CancelamentoAssinaturaResponse;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemocaoPlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    public void realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(String asaasId) {

        log.debug("Método de serviço responsável pela cancelamento de assinatura na integradora ASAAS acessado");
        ResponseEntity<CancelamentoAssinaturaResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de assinatura para a integradora ASAAS...");
            responseAsaas =
                    planoAsaasProxy.cancelarAssinatura(asaasId, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesPlano.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesPlano.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de cancelamento da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesPlano.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Cancelamento de assinatura ASAAS realizada com sucesso");

        CancelamentoAssinaturaResponse cancelamentoAssinaturaResponse = responseAsaas.getBody();

        if (cancelamentoAssinaturaResponse == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }
}
