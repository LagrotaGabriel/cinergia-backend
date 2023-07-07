package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.pagamento.response.PagamentoPageResponse;
import br.com.backend.models.dto.pagamento.response.PagamentoResponse;
import br.com.backend.services.pagamento.PagamentoService;
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
@Api(value = "Esta API disponibiliza os endpoints de CRUD da entidade Pagamento")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/pagamento")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping("/cliente/{id}")
    @ApiOperation(
            value = "Busca paginada por pagamentos cadastrados em um determinado cliente",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados em um cliente " +
                    "da empresa logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de pagamentos do cliente foi realizada com sucesso",
                    response = PagamentoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginadosDoCliente(
            @PathVariable(value = "id") Long id,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por pagamentos do cliente acessado");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosDoCliente(
                jwtUtil.obtemEmpresaAtiva(req), pageable, id));
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Busca paginada por pagamentos cadastrados",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados na empresa " +
                    "logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de pagamentos foi realizada com sucesso",
                    response = PagamentoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginadosDoPlano(
            @PathVariable(value = "id") Long id,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por pagamentos acessado");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosDoPlano(
                jwtUtil.obtemEmpresaAtiva(req), pageable, id));
    }

    @GetMapping
    @ApiOperation(
            value = "Busca paginada por pagamentos cadastrados",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados na empresa " +
                    "logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de pagamentos foi realizada com sucesso",
                    response = PagamentoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginados(
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por pagamentos do cliente acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentos(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }

    @GetMapping("/aprovados")
    @ApiOperation(
            value = "Busca paginada por pagamentos realizados cadastrados",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos realizados cadastrados na empresa " +
                    "logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de pagamentos realizados foi realizada com sucesso",
                    response = PagamentoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosRealizadosPaginados(
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por pagamentos realizados foi acessada");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosRealizados(
                jwtUtil.obtemEmpresaAtiva(req), pageable));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Remoção de pagamento por ID",
            notes = "Esse endpoint tem como objetivo realizar a remoção de um pagamento por ID",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A remoção do pagamento foi realizada com sucesso",
                    response = PagamentoResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PagamentoResponse> realizaRemocaoDoPagamento(@PathVariable(value = "id") Long id,
                                                                       HttpServletRequest req) {
        log.info("Endpoint de remoção de pagamento foi acessada");
        return ResponseEntity.ok().body(pagamentoService.cancelaPagamento(id, jwtUtil.obtemEmpresaAtiva(req)));
    }


}
