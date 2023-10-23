package br.com.backend.modules.pagamento.proxy;

import br.com.backend.modules.pagamento.proxy.models.CancelamentoPagamentoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ASAAS-PAGAMENTO", url = "${URL_ASAAS}")
public interface PagamentoAsaasProxy {
    @DeleteMapping(value = "/api/v3/payments/{id}")
    ResponseEntity<CancelamentoPagamentoResponse> cancelarCobranca(@PathVariable(value = "id") String id,
                                                                   @RequestHeader(value = "access_token") String accessToken);
}
