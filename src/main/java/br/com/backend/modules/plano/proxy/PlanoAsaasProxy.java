package br.com.backend.modules.plano.proxy;

import br.com.backend.modules.plano.proxy.operations.atualizacao.models.request.AtualizaAssinaturaAsaasRequest;
import br.com.backend.modules.plano.proxy.operations.criacao.models.request.CriaPlanoAsaasRequest;
import br.com.backend.modules.plano.proxy.operations.atualizacao.models.response.AtualizaAssinaturaAsaasResponse;
import br.com.backend.modules.plano.proxy.operations.remocao.response.CancelamentoAssinaturaResponse;
import br.com.backend.modules.plano.proxy.operations.consulta.response.ConsultaAssinaturaResponse;
import br.com.backend.modules.plano.proxy.operations.criacao.models.response.CriaPlanoAsaasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ASAAS-PLANO", url = "${URL_ASAAS}")
public interface PlanoAsaasProxy {
    @PostMapping(value = "api/v3/subscriptions")
    ResponseEntity<CriaPlanoAsaasResponse> cadastraNovaAssinatura(@RequestBody CriaPlanoAsaasRequest criaPlanoAsaasRequest,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @PostMapping(value = "api/v3/customers/{id}")
    ResponseEntity<AtualizaAssinaturaAsaasResponse> atualizaAssinatura(@PathVariable String id,
                                                                       @RequestBody AtualizaAssinaturaAsaasRequest atualizaAssinaturaAsaasRequest,
                                                                       @RequestHeader(value = "access_token") String accessToken);

    @GetMapping(value = "api/v3/subscriptions/{idAssinatura}")
    ResponseEntity<ConsultaAssinaturaResponse> consultaAssinatura(@PathVariable String idAssinatura,
                                                                  @RequestHeader(value = "access_token") String accessToken);

    @DeleteMapping(value = "/api/v3/subscriptions/{id}")
    ResponseEntity<CancelamentoAssinaturaResponse> cancelarAssinatura(@PathVariable(value = "id") String id,
                                                                      @RequestHeader(value = "access_token") String accessToken);
}
