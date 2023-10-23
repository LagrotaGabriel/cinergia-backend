package br.com.backend.modules.transferencia.proxy.operations.criacao;

import br.com.backend.exceptions.custom.FeignConnectionException;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.proxy.TransferenciaAsaasProxy;
import br.com.backend.modules.transferencia.proxy.operations.criacao.models.request.CriacaoTransferenciaPixAsaasRequest;
import br.com.backend.modules.transferencia.proxy.operations.criacao.models.response.CriacaoTransferenciaPixAsaasResponse;
import br.com.backend.modules.transferencia.utils.ConstantesTransferencia;
import br.com.backend.util.Constantes;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CriacaoTransferenciaAsaasProxyImpl {

    @Autowired
    TransferenciaAsaasProxy transferenciaAsaasProxy;

    public String realizaCriacaoTransferenciaAsaas(TransferenciaRequest transferenciaRequest) {

        //TODO ELABORAR LÓGICA DE DINHEIRO NA CONTA EM 1 DIA CORRIDO

        log.info("Método de serviço responsável pela criação de transferência na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto transferePixAsaasRequest...");
        CriacaoTransferenciaPixAsaasRequest criacaoTransferenciaPixAsaasRequest = CriacaoTransferenciaPixAsaasRequest.builder()
                .value(transferenciaRequest.getValor())
                .pixAddressKey(transferenciaRequest.getChavePix())
                .pixAddressKeyType(transferenciaRequest.getTipoChavePix())
                .description(transferenciaRequest.getDescricao())
                .scheduleDate(null)
                .build();

        ResponseEntity<CriacaoTransferenciaPixAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de criação de transferência para a integradora ASAAS...");
            responseAsaas =
                    transferenciaAsaasProxy.transferirPix(criacaoTransferenciaPixAsaasRequest, System.getenv("TOKEN_ASAAS"));
        } catch (FeignException e) {
            log.error(ConstantesTransferencia.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                    + e.getMessage());
            if (e.status() == 409)
                throw new FeignConnectionException("Ocorreu um erro interno ao tentar realizar sua transferência. Tente novamente em 5 minutos");
            else
                throw new InvalidRequestException(ConstantesTransferencia.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                        + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da transferência na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesTransferencia.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                    + responseAsaas.getBody());
        }
        log.info("Criação de transferência ASAAS realizada com sucesso");

        CriacaoTransferenciaPixAsaasResponse criacaoTransferenciaPixAsaasResponse = responseAsaas.getBody();

        if (criacaoTransferenciaPixAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.info("Retornando id da transferência gerada: {}", criacaoTransferenciaPixAsaasResponse.getId());
        return criacaoTransferenciaPixAsaasResponse.getId();
    }

}
