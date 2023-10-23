package br.com.backend.modules.plano.proxy.operations.consulta.impl;

import br.com.backend.exceptions.custom.FeignConnectionException;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.plano.proxy.PlanoAsaasProxy;
import br.com.backend.modules.plano.proxy.operations.consulta.response.ConsultaAssinaturaResponse;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsultaPlanoAsaasProxyImpl {
    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

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
