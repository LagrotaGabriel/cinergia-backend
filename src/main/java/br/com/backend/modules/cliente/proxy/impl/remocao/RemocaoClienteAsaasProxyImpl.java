package br.com.backend.modules.cliente.proxy.impl.remocao;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.proxy.ClienteAsaasProxy;
import br.com.backend.modules.cliente.proxy.models.response.RemoveClienteAsaasResponse;
import br.com.backend.modules.cliente.proxy.impl.remocao.utils.RemocaoClienteAsaasProxyUtils;
import br.com.backend.modules.cliente.utils.ConstantesClientes;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemocaoClienteAsaasProxyImpl {

    @Autowired
    ClienteAsaasProxy clienteAsaasProxy;

    @Autowired
    RemocaoClienteAsaasProxyUtils remocaoClienteAsaasProxyUtils;

    public void realizaRemocaoDoClienteNaIntegradoraAsaas(String asaasId) {

        log.info("Método de serviço responsável pela remoção de cliente na integradora ASAAS acessado");
        ResponseEntity<RemoveClienteAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de remoção cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.removerCliente(asaasId, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesClientes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesClientes.ERRO_REMOCAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        log.info("Realizando validações referente à resposta da integradora...");
        remocaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);
        log.info("Validações realizadas com sucesso. Cliente removido da ASAAS com sucesso.");
    }

}
