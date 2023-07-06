package br.com.backend.proxy;

import br.com.backend.proxy.cliente.request.CriaClienteAsaasRequest;
import br.com.backend.proxy.cliente.response.CriaClienteAsaasResponse;
import br.com.backend.proxy.plano.request.CriaPlanoAsaasRequest;
import br.com.backend.proxy.plano.response.CriaPlanoAsaasResponse;
import br.com.backend.proxy.plano.response.consulta.ConsultaAssinaturaResponse;
import br.com.backend.proxy.transferencia.request.TransferePixAsaasRequest;
import br.com.backend.proxy.transferencia.response.TransferePixAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ASAAS", url = "${URL_ASAAS}")
public interface AsaasProxy {

    @PostMapping(value = "api/v3/subscriptions")
    ResponseEntity<CriaPlanoAsaasResponse> cadastraNovaAssinatura(@RequestBody CriaPlanoAsaasRequest criaPlanoAsaasRequest,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/customers")
    ResponseEntity<CriaClienteAsaasResponse> cadastraNovoCliente(@RequestBody CriaClienteAsaasRequest criaClienteAsaasRequest,
                                                                 @RequestHeader(value = "access_token") String accessToken);

    @GetMapping(value = "api/v3/subscriptions/{idAssinatura}")
    ResponseEntity<ConsultaAssinaturaResponse> consultaAssinatura(@PathVariable String idAssinatura,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/transfers")
    ResponseEntity<TransferePixAsaasResponse> transferirPix(@RequestBody TransferePixAsaasRequest transferePixAsaasRequest,
                                                            @RequestHeader(value = "access_token") String accessToken);

}
