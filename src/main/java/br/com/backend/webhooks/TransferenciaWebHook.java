package br.com.backend.webhooks;

import br.com.backend.proxy.webhooks.transferencia.AtualizacaoTransferenciaWebHook;
import br.com.backend.services.exceptions.UnauthorizedAccessException;
import br.com.backend.services.transferencia.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("${default.webhook.path}/transferencia")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class TransferenciaWebHook {

    @Autowired
    WebHookTokenValidation webHookTokenValidation;

    @Autowired
    TransferenciaService transferenciaService;

    /**
     * Recebe alteração de status de pagamento
     * Este método tem como objetivo receber uma atualização do status de transferência e realizar o acionamento da lógica
     * cabível para o evento em questão
     *
     * @param atualizacaoTransferenciaWebHook Objeto contendo todos os atributos enviados pela integradora ASAAS para que
     *                                        seja realizada a atualização da transferência com base em sua atualização de status
     * @param token                           Token de acesso da integradora à API. Necessário para que método possa ser executado até o fim
     * @return Retorna objeto do tipo HttpStatus, que deverá informar status da requisição
     */
    @PostMapping
    @Tag(name = "Recebimento de status de transferência via PIX")
    @Operation(summary = "Esse endpoint tem como objetivo receber atualizações no status das transferências via " +
            "PIX por parte da integradora ASAAS", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição finalizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpStatus.class))}),
            @ApiResponse(responseCode = "403",
                    description = "Nenhum token de acesso foi recebido",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))}),
            @ApiResponse(responseCode = "403",
                    description = "O token de acesso recebido está incorreto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))}),
    })
    public ResponseEntity<HttpStatus> recebeStatusTransferenciaAsaas(@RequestBody AtualizacaoTransferenciaWebHook atualizacaoTransferenciaWebHook,
                                                                     @RequestHeader(value = "asaas-access-token") String token) {
        log.info("Webhook ASAAS de atualização do status de transferência recebido: {}", atualizacaoTransferenciaWebHook);
        webHookTokenValidation.realizaValidacaoToken(token);
        transferenciaService.realizaTratamentoWebhookTransferencia(atualizacaoTransferenciaWebHook);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
