package br.com.backend.proxy;

import br.com.backend.proxy.cliente.request.CriaClienteAsaasRequest;
import br.com.backend.proxy.cliente.response.CriaClienteAsaasResponse;
import br.com.backend.proxy.empresa.request.SubContaAsaasRequest;
import br.com.backend.proxy.empresa.response.ConsultaSubContaResponse;
import br.com.backend.proxy.empresa.response.SubContaAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS", url = "${URL_ASAAS}")
public interface AsaasProxy {

    @PostMapping(value = "api/v3/accounts")
    ResponseEntity<SubContaAsaasResponse> cadastraNovaSubConta(@RequestBody SubContaAsaasRequest subContaAsaasRequest,
                                                               @RequestHeader(value = "access_token") String accessToken);

    @GetMapping(value = "api/v3/accounts?5bf81f2a-1295-4a41-be18-b32e945667cb")
    ResponseEntity<ConsultaSubContaResponse> consultaSubConta(@RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/customers")
    ResponseEntity<CriaClienteAsaasResponse> cadastraNovoCliente(@RequestBody CriaClienteAsaasRequest criaClienteAsaasRequest,
                                                                 @RequestHeader(value = "access_token") String accessToken);

}
