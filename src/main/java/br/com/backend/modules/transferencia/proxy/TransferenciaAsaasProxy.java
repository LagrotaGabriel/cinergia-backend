package br.com.backend.modules.transferencia.proxy;

import br.com.backend.modules.transferencia.proxy.models.request.TransferePixAsaasRequest;
import br.com.backend.modules.transferencia.proxy.models.response.TransferePixAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS_TRANSFERENCIA", url = "${URL_ASAAS}")
public interface TransferenciaAsaasProxy {
    @PostMapping(value = "api/v3/transfers")
    ResponseEntity<TransferePixAsaasResponse> transferirPix(@RequestBody TransferePixAsaasRequest transferePixAsaasRequest,
                                                            @RequestHeader(value = "access_token") String accessToken);
}
