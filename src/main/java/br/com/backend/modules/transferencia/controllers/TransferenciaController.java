package br.com.backend.modules.transferencia.controllers;

import br.com.backend.config.security.user.UserSS;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.dto.response.TransferenciaResponse;
import br.com.backend.modules.transferencia.models.dto.response.page.TransferenciaPageResponse;
import br.com.backend.modules.transferencia.services.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/transferencia")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class TransferenciaController {

    @Autowired
    TransferenciaService transferenciaService;

    /**
     * Cadastro de transferência via pix
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de uma nova
     * transferência via pix
     *
     * @param userDetails          Dados do usuário logado na sessão atual
     * @param transferenciaRequest Objeto contendo todos os atributos necessários para a criação de uma nova
     *                             transferência via pix
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Criação de nova transferência via PIX")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de uma transferência via PIX", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A criação da transferência foi realizada com sucesso"),
    })
    public ResponseEntity<TransferenciaResponse> criaNovaTransferencia(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TransferenciaRequest transferenciaRequest) {

        log.info("Endpoint de criação de nova transferência acessado");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transferenciaService.criaTransferencia(
                        ((UserSS) userDetails).getId(),
                        transferenciaRequest));
    }

    /**
     * Busca paginada de transferÊncias
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de transferências
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo TransferenciaPageResponse, que possui informações da paginação e a lista de
     * transferências encontradas inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca de transferências paginadas")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção de transferências de uma empresa " +
            "de forma paginada", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de transferências foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransferenciaPageResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de obtenção das transferências da empresa",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<TransferenciaPageResponse> obtemTransferenciasEmpresa(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        log.info("Método controlador de obtenção de transferências da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transferenciaService.obtemTransferenciasPaginadas((
                                (UserSS) userDetails).getId(),
                        pageable));
    }

}
