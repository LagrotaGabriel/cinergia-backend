package br.com.backend.modules.pagamento.proxy.impl;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.proxy.PagamentoAsaasProxy;
import br.com.backend.modules.pagamento.proxy.models.CancelamentoPagamentoResponse;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PagamentoAsaasProxyImpl {

    @Autowired
    PagamentoAsaasProxy pagamentoAsaasProxy;

    public void realizaCancelamentoDeCobrancaNaIntegradoraAsaas(PagamentoEntity pagamentoEntity) {

        log.debug("Método de serviço responsável pela cancelamento de pagamento na integradora ASAAS acessado");
        ResponseEntity<CancelamentoPagamentoResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de pagamento para a integradora ASAAS...");
            responseAsaas =
                    pagamentoAsaasProxy.cancelarCobranca(pagamentoEntity.getIdAsaas(), System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(ConstantesPagamento.ERRO_CANCELAMENTO_PAGAMENTO_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesPagamento.ERRO_CANCELAMENTO_PAGAMENTO_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora no cancelamento do pagamento é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de cancelamento do pagamento na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesPagamento.ERRO_CANCELAMENTO_PAGAMENTO_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Cancelamento de pagamento ASAAS realizada com sucesso");

        CancelamentoPagamentoResponse cancelamentoAssinaturaResponse = responseAsaas.getBody();

        if (cancelamentoAssinaturaResponse == null) {
            log.error("O valor retornado pela integradora no cancelamento do pagamento é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

}
