package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.services.plano.PlanoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints de CRUD da entidade Plano")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/plano")
public class PlanoController {

    @Autowired
    PlanoService planoService;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping
    @ApiOperation(
            value = "Busca paginada por planos cadastrados",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de planos cadastrados na empresa " +
                    "logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de planos foi realizada com sucesso",
                    response = PlanoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginados(
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por planos acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanos(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }

}
