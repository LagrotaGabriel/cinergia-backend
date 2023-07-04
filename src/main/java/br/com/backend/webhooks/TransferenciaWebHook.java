package br.com.backend.webhooks;

import br.com.backend.proxy.webhooks.transferencia.AtualizacaoTransferenciaWebHook;
import br.com.backend.services.transferencia.TransferenciaService;
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
import java.text.ParseException;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints de webhook de transferência")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("webhook/v1/transferencia")
public class TransferenciaWebHook {

    @Autowired
    WebHookTokenValidation webHookTokenValidation;

    @Autowired
    TransferenciaService transferenciaService;


    @ApiOperation(
            value = "Recebimento de status de transferência",
            notes = "Esse endpoint tem como objetivo receber atualizações no status das transferências PIX por " +
                    "parte da integradora ASAAS",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Requisição finalizada com sucesso", response = HttpStatus.class)
    })
    @PostMapping
    public ResponseEntity<HttpStatus> recebeStatusTransferenciaAsaas(@RequestBody AtualizacaoTransferenciaWebHook atualizacaoTransferenciaWebHook,
                                                                     @RequestHeader(value = "asaas-access-token") String token)
            throws ParseException {
        log.info("Webhook ASAAS de atualização do status de transferência recebido: {}", atualizacaoTransferenciaWebHook);
        webHookTokenValidation.realizaValidacaoToken(token);
        transferenciaService.realizaTratamentoWebhookTransferencia(atualizacaoTransferenciaWebHook);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
