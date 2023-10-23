package br.com.backend.modules.transferencia.proxy.operations.cancelamento;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.transferencia.proxy.TransferenciaAsaasProxy;
import br.com.backend.modules.transferencia.proxy.operations.cancelamento.response.CancelamentoPixAsaasResponse;
import br.com.backend.modules.transferencia.utils.ConstantesTransferencia;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CancelamentoTransferenciaAsaasProxyImpl {

    @Autowired
    TransferenciaAsaasProxy transferenciaAsaasProxy;

    public void realizaCancelamentoDeTransferenciaNaIntegradoraAsaas(String asaasId) {

        log.debug("Método de serviço responsável pelo cancelamento de transferência na integradora ASAAS acessado");
        ResponseEntity<CancelamentoPixAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de transferência para a integradora ASAAS...");
            responseAsaas =
                    transferenciaAsaasProxy.cancelarTransferenciaPix(asaasId, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesTransferencia.ERRO_CANCELAMENTO_TRANSFERENCIA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesTransferencia.ERRO_CANCELAMENTO_TRANSFERENCIA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na cancelamento da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de cancelamento da transferência na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesTransferencia.ERRO_CANCELAMENTO_TRANSFERENCIA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Cancelamento de transferência ASAAS realizada com sucesso");

        CancelamentoPixAsaasResponse cancelamentoPixAsaasResponse = responseAsaas.getBody();

        if (cancelamentoPixAsaasResponse == null) {
            log.error("O valor retornado pela integradora na cancelamento da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

}
