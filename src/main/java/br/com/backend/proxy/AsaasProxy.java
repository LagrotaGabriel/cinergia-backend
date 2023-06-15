package br.com.backend.proxy;

import br.com.backend.proxy.empresa.request.SubContaAsaasRequest;
import br.com.backend.proxy.empresa.response.SubContaAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS", url = "${URL_ASAAS}")
public interface AsaasProxy {

    @PostMapping(value = "api/v3/accounts")
    ResponseEntity<SubContaAsaasResponse> cadastraNovaSubConta(@RequestBody SubContaAsaasRequest subContaAsaasRequest,
                                                               @RequestHeader(value = "access_token") String accessToken);

}
