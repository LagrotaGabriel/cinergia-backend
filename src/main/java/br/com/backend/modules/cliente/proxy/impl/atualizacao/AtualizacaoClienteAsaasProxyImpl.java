package br.com.backend.modules.cliente.proxy.impl.atualizacao;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.proxy.ClienteAsaasProxy;
import br.com.backend.modules.cliente.proxy.impl.atualizacao.utils.AtualizacaoClienteAsaasProxyUtils;
import br.com.backend.modules.cliente.proxy.models.request.ClienteAsaasRequest;
import br.com.backend.modules.cliente.proxy.models.response.ClienteAsaasResponse;
import br.com.backend.modules.cliente.utils.ConstantesClientes;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AtualizacaoClienteAsaasProxyImpl {

    @Autowired
    ClienteAsaasProxy clienteAsaasProxy;

    @Autowired
    AtualizacaoClienteAsaasProxyUtils atualizacaoClienteAsaasProxyUtils;

    public void realizaAtualizacaoClienteAsaas(UUID uuidEmpresaSessao, String asaasId, ClienteRequest clienteRequest) {

        log.info("Método de serviço responsável pela atualização de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto criaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequest(uuidEmpresaSessao, clienteRequest);
        log.info("Objeto CriaClienteAsaasRequest construído com sucesso");

        ResponseEntity<ClienteAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de atualização de cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.atualizaCliente(asaasId, clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesClientes.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesClientes.ERRO_ATUALIZACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        log.info("Realizando validações referente à resposta da integradora...");
        atualizacaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);
        log.info("Validações realizadas com sucesso. Cliente atualizado na ASAAS com sucesso.");
    }


}
