package br.com.backend.modules.cliente.proxy.impl.remocao.utils;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.proxy.models.response.RemoveClienteAsaasResponse;
import br.com.backend.modules.cliente.utils.ConstantesClientes;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemocaoClienteAsaasProxyUtils {

    public void realizaValidacaoResponseAsaas(ResponseEntity<RemoveClienteAsaasResponse> responseEntity) {
        if (responseEntity == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de remoção do cliente na integradora de pagamentos: {}",
                    responseEntity.getBody());
            throw new InvalidRequestException(ConstantesClientes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + responseEntity.getBody());
        }

        if (responseEntity.getBody() == null) {
            log.error("O valor retornado pela integradora na criação do cliente é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }
    }

}
