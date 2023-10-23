package br.com.backend.modules.transferencia.proxy;

import br.com.backend.modules.transferencia.proxy.operations.cancelamento.response.CancelamentoPixAsaasResponse;
import br.com.backend.modules.transferencia.proxy.operations.criacao.models.request.CriacaoTransferenciaPixAsaasRequest;
import br.com.backend.modules.transferencia.proxy.operations.criacao.models.response.CriacaoTransferenciaPixAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS-TRANSFERENCIA", url = "${URL_ASAAS}")
public interface TransferenciaAsaasProxy {
    @PostMapping(value = "api/v3/transfers")
    ResponseEntity<CriacaoTransferenciaPixAsaasResponse> transferirPix(@RequestBody CriacaoTransferenciaPixAsaasRequest criacaoTransferenciaPixAsaasRequest,
                                                                       @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/transfers/{id}/cancel")
    ResponseEntity<CancelamentoPixAsaasResponse> cancelarTransferenciaPix(@PathVariable("id") String id,
                                                                          @RequestHeader(value = "access_token") String accessToken);
}
