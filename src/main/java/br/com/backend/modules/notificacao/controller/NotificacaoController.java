package br.com.backend.modules.notificacao.controller;

import br.com.backend.config.security.user.UserSS;
import br.com.backend.modules.notificacao.models.dto.NotificacaoResponse;
import br.com.backend.modules.notificacao.services.NotificacaoService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * NotificacaoController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade NotificacaoEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/notificacao")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class NotificacaoController {

    @Autowired
    NotificacaoService notificacaoService;

    /**
     * Obtenção das notificações da empresa
     * Este método busca providenciar o retorno das notificações da empresa responsável pelo envio da requisição
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna lista de notificações da empresa
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Obtenção de notificações da empresa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção das notificações da empresa",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Notificações obtidas com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificacaoResponse.class))})
    })
    public ResponseEntity<List<NotificacaoResponse>> obtemNotificacoesEmpresa(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Método controlador de obtenção de notificações da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacaoService.implementaBuscaDeNotificacoesEmpresa(
                        ((UserSS) userDetails).getId()));
    }

    /**
     * Setar notificações como lidas
     * Este método tem como objetivo implementar o acesso à lógica de setagem de notificações como lidas
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @return Retorna ResponseEntity vazio, somente com o status da requisição
     */
    @GetMapping("/marcar-como-lido")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Seta notificações da empresa como lidas")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a setagem das notificações da empresa para lidas",
            method = "GET")
    public ResponseEntity<?> setaNotificacoesEmpresaComoLidas(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Método controlador de setagem de notificações da empresa como lidas acessado");
        notificacaoService.realizaSetagemDasNotificacoesDaEmpresaComoLidas(((UserSS) userDetails).getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
