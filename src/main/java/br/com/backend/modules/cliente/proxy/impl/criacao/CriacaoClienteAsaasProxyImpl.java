package br.com.backend.modules.cliente.proxy.impl.criacao;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.proxy.ClienteAsaasProxy;
import br.com.backend.modules.cliente.proxy.models.request.ClienteAsaasRequest;
import br.com.backend.modules.cliente.proxy.models.response.ClienteAsaasResponse;
import br.com.backend.modules.cliente.proxy.impl.criacao.utils.CriacaoClienteAsaasProxyUtils;
import br.com.backend.modules.cliente.utils.ConstantesClientes;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class CriacaoClienteAsaasProxyImpl {

    @Autowired
    ClienteAsaasProxy clienteAsaasProxy;

    @Autowired
    CriacaoClienteAsaasProxyUtils criacaoClienteAsaasProxyUtils;

    public String realizaCriacaoClienteAsaas(UUID uuidEmpresaSessao, ClienteRequest clienteRequest) {

        log.info("Método de serviço responsável pela criação de cliente na integradora ASAAS acessado");

        log.info("Iniciando construção do objeto criaClienteAsaasRequest...");
        ClienteAsaasRequest clienteAsaasRequest = new ClienteAsaasRequest()
                .constroiObjetoCriaClienteAsaasRequest(uuidEmpresaSessao, clienteRequest);
        log.info("Objeto CriaClienteAsaasRequest construído com sucesso");

        ResponseEntity<ClienteAsaasResponse> responseAsaas;

        try {
            log.info("Realizando envio de requisição de criação de cliente para a integradora ASAAS...");
            responseAsaas =
                    clienteAsaasProxy.cadastraNovoCliente(clienteAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesClientes.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesClientes.ERRO_CRIACAO_CLIENTE_ASAAS
                    + e.getMessage());
        }

        log.info("Realizando validações referente à resposta da integradora...");
        criacaoClienteAsaasProxyUtils.realizaValidacaoResponseAsaas(responseAsaas);

        ClienteAsaasResponse clienteAsaasResponse = responseAsaas.getBody();

        log.info("Criação de cliente na integradora ASAAS realizada com sucesso. Retornando id do " +
                "cliente gerado: {}", Objects.requireNonNull(clienteAsaasResponse).getId());
        return clienteAsaasResponse.getId();
    }

}
