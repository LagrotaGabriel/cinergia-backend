package br.com.backend.modules.cliente.proxy;

import br.com.backend.modules.cliente.proxy.models.request.ClienteAsaasRequest;
import br.com.backend.modules.cliente.proxy.models.response.ClienteAsaasResponse;
import br.com.backend.modules.cliente.proxy.models.response.RemoveClienteAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ASAAS-CLIENTES", url = "${URL_ASAAS}")
public interface ClienteAsaasProxy {

    @PostMapping(value = "api/v3/customers")
    ResponseEntity<ClienteAsaasResponse> cadastraNovoCliente(@RequestBody ClienteAsaasRequest clienteAsaasRequest,
                                                             @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/customers/{id}")
    ResponseEntity<ClienteAsaasResponse> atualizaCliente(@PathVariable String id,
                                                         @RequestBody ClienteAsaasRequest clienteAsaasRequest,
                                                         @RequestHeader(value = "access_token") String accessToken);

    @DeleteMapping(value = "/api/v3/customers/{id}")
    ResponseEntity<RemoveClienteAsaasResponse> removerCliente(@PathVariable(value = "id") String id,
                                                              @RequestHeader(value = "access_token") String accessToken);

}
