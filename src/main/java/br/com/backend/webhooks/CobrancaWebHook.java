package br.com.backend.webhooks;

import br.com.backend.proxy.webhooks.cobranca.AtualizacaoCobrancaWebHook;
import br.com.backend.services.pagamento.PagamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints de webhook de cobrança")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("webhook/v1/cobranca")
public class CobrancaWebHook {

    @Autowired
    WebHookTokenValidation webHookTokenValidation;

    @Autowired
    PagamentoService pagamentoService;

    @ApiOperation(
            value = "Recebimento de status de pagamento",
            notes = "Esse endpoint tem como objetivo receber atualizações no status dos pagamentos das assinaturas por " +
                    "parte da integradora ASAAS",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Requisição finalizada com sucesso", response = HttpStatus.class)
    })
    @PostMapping(value = "/pagamento")
    public ResponseEntity<HttpStatus> recebeStatusPagamentoAsaas(@RequestBody AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                                 @RequestHeader(value = "asaas-access-token") String token) {
        log.info("Webhook ASAAS de atualização do status de cobrança recebido: {}", atualizacaoCobrancaWebHook);
//        webHookTokenValidation.realizaValidacaoToken(token);
//        pagamentoService.realizaTratamentoWebhookCobranca(atualizacaoCobrancaWebHook);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
