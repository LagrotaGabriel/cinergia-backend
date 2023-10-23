package br.com.backend.modules.pagamento.controllers;

import br.com.backend.config.security.user.UserSS;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.modules.pagamento.models.responses.PagamentoResponse;
import br.com.backend.modules.pagamento.models.responses.page.PagamentoPageResponse;
import br.com.backend.modules.pagamento.services.PagamentoService;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * PagamentoController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade PagamentoController
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/pagamento")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    /**
     * Busca paginada de pagamentos
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de pagamentos
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de pagamentos por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo PagamentoPageResponse, que possui informações da paginação e a lista de pagamentos
     * encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por pagamentos cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados na empresa " +
            "logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de pagamentos foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPageResponse.class))}),
    })
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginados(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable) {

        log.info("Endpoint de busca paginada por pagamentos acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentos(
                ((UserSS) userDetails).getId(),
                busca,
                pageable));
    }

    /**
     * Busca paginada de pagamentos aprovados
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de
     * pagamentos que possuam o status aprovado
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo PagamentoPageResponse, que possui informações da paginação e a lista de pagamentos
     * encontrados inserida em seu body
     */
    @GetMapping("/aprovados")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por pagamentos aprovados cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos aprovados " +
            "cadastrados na empresa logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de pagamentos aprovados foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPageResponse.class))}),
    })
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosAprovadosPaginados(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {

        log.info("Endpoint de busca paginada por pagamentos realizados foi acessada");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosRealizados(
                ((UserSS) userDetails).getId(),
                pageable));
    }

    /**
     * Busca pagamentos paginados pelo uuid do cliente
     * Este método deve retornar um objeto do tipo PagamentoPageResponse, que deve conter as informações da paginação
     * e uma lista de Pagamentos do cliente correspondente com o ID enviado através da requisição
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidCliente ID do cliente. Utilizado para encontrar o cliente em questão e retornar seus pagamentos
     * @param pageable    Objeto contendo os atributos e especificações da paginação
     * @return Retorna objeto do tipo PagamentoPageResponse, contendo informações da paginação e uma lista de objetos
     * do tipo PagamentoResponse pertencentes ao cliente buscado através do uuid recebido pelo parâmetro
     */
    @GetMapping("/cliente/{uuid}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por pagamentos cadastrados em um determinado cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados em um " +
            "cliente da empresa logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de pagamentos do cliente foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPageResponse.class))})
    })
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginadosDoCliente(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(value = "uuidCliente") UUID uuidCliente,
            Pageable pageable) {

        log.info("Endpoint de busca paginada por pagamentos do cliente acessado");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosDoCliente(
                ((UserSS) userDetails).getId(),
                uuidCliente,
                pageable));
    }

    /**
     * Obtenção dos pagamentos paginados do plano
     * Este método deve retornar um objeto do tipo PagamentoPageResponse, que deve conter as informações da paginação
     * e uma lista de Pagamentos do plano correspondente com o ID enviado através da requisição
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidPlano   ID do plano. Utilizado para encontrar o plano em questão e retornar seus pagamentos
     * @param pageable    Objeto contendo os atributos e especificações da paginação
     * @return Retorna objeto do tipo PagamentoPageResponse, contendo informações da paginação e uma lista de objetos
     * do tipo PagamentoResponse pertencentes ao plano buscado através do id recebido pelo parâmetro
     */
    @GetMapping("/{uuidPlano}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada de pagamentos por plano")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de pagamentos cadastrados no " +
            "plano que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de pagamentos foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagamentoPageResponse.class))})
    })
    public ResponseEntity<PagamentoPageResponse> obtemPagamentosPaginadosDoPlano(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(value = "uuidPlano") UUID uuidPlano,
            Pageable pageable) {

        log.info("Endpoint de busca paginada por pagamentos acessado");
        return ResponseEntity.ok().body(pagamentoService.realizaBuscaPaginadaPorPagamentosDoPlano(
                ((UserSS) userDetails).getId(),
                uuidPlano,
                pageable));
    }

    /**
     * Exclusão de pagamento
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de pagamento por uuid
     *
     * @param userDetails   Dados do usuário logado na sessão atual
     * @param uuidPagamento Id do pagamento a ser removido
     * @return Retorna objeto Pagamento removido convertido para o tipo PagamentoResponse
     */
    @DeleteMapping("/{uuidPagamento}")
    @Tag(name = "Remoção de pagamento")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um pagamento", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Pagamento excluído com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum pagamento foi encontrado com o uuid informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O pagamento selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum pagamento foi encontrado para remoção",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um pagamento que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<PagamentoResponse> realizaRemocaoDoPagamento(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable(value = "uuidPagamento") UUID uuidPagamento) {

        log.info("Endpoint de remoção de pagamento foi acessada");
        return ResponseEntity.ok().body(pagamentoService.cancelaPagamento(
                ((UserSS) userDetails).getId(),
                uuidPagamento));
    }

    //TODO MÉTODO PARA TESTE. REMOVER DEPOIS
    @PostMapping("/novo-pagamento/{uuidPlano}")
    public ResponseEntity<PlanoEntity> obtemPagamentosPaginados(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("uuidPlano") UUID uuidPlano) {
        return ResponseEntity.ok().body(pagamentoService.injetaPagamentoNoPlano(
                ((UserSS) userDetails).getId(),
                uuidPlano));
    }

}
