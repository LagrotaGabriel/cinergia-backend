package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.notificacao.NotificacaoResponse;
import br.com.backend.services.notificacao.NotificacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints da entidade Notificacao")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/notificacao")
public class NotificacaoController {

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping
    @ApiOperation(
            value = "Obtenção de notificações da empresa",
            notes = "Esse endpoint tem como objetivo realizar a obtenção das notificações da empresa",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Notificações obtidas com sucesso", response = NotificacaoResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<List<NotificacaoResponse>> obtemNotificacoesEmpresa(
            @PageableDefault(size = 20, sort = {"dataCadastro", "horaCadastro"}, direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest req) {

        log.info("Método controlador de obtenção de notificações da empresa acessado");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificacaoService.implementaBuscaDeNotificacoesEmpresa(pageable, jwtUtil.obtemEmpresaAtiva(req)));
    }

}
