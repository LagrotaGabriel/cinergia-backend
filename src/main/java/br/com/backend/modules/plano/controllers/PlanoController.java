package br.com.backend.modules.plano.controllers;

import br.com.backend.config.security.user.UserSS;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.models.dto.response.DadosPlanoResponse;
import br.com.backend.modules.plano.models.dto.response.page.PlanoPageResponse;
import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import br.com.backend.modules.plano.services.PlanoService;
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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/plano")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PlanoController {

    @Autowired
    PlanoService planoService;

    /**
     * Criação de novo plano
     * Este método permite que o acionamento lógico do método de criação de novo plano de assinatura para um cliente
     * seja acionado
     *
     * @param userDetails  Dados do usuário logado na sessão atual
     * @param uuidCliente  Chave primária de identificação do cliente
     * @param planoRequest Objeto que deve conter todos os atributos necessários para a criação de um novo plano
     * @return Objeto Plano criado convertido para objeto do tipo response
     */
    @PostMapping("/{uuidCliente}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Criação de um novo plano para um determinado cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um novo plano de assinatura para um " +
            "determinado cliente", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "A criação de um novo plano foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o uuidCliente informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PlanoResponse> criaNovoPlano(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable(value = "uuidCliente") UUID uuidCliente,
                                                       @Valid @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de criação de novo plano acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                planoService.criaNovoPlano(
                        ((UserSS) userDetails).getId(),
                        uuidCliente,
                        planoRequest)
        );
    }


    /**
     * Busca paginada de planos do cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de planos
     * relacionados com o cliente encontrado através do id informado via parâmetro
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidEmpresa Deve ser utilizado para identificar um cliente e localizar os planos cadastrados em seu perfil
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo PlanoPageResponse, que possui informações da paginação e a lista de planos
     * encontrados inserida em seu body
     */
    @GetMapping("/cliente/{uuidCliente}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por planos cadastrados no perfil do cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de planos cadastrados no perfil de " +
            "um cliente específico buscado por id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de planos foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoPageResponse.class))}),
    })
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginadosDoCliente(Pageable pageable,
                                                                           @AuthenticationPrincipal UserDetails userDetails,
                                                                           @PathVariable(value = "uuidCliente") UUID uuidEmpresa) {
        log.info("Endpoint de busca paginada por planos acessado");
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanosDoCliente(
                pageable,
                ((UserSS) userDetails).getId(),
                uuidEmpresa));
    }


    /**
     * Busca paginada de planos
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de planos
     * cadastrados
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param busca       Parâmetro opcional. Recebe uma string para busca de planos por atributos específicos
     * @param pageable    Contém especificações da paginação, como tamanho da página, página atual, etc
     * @return Retorna objeto do tipo PlanoPageResponse, que possui informações da paginação e a lista de planos
     * encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por planos cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de planos cadastrados na empresa " +
            "logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de planos foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoPageResponse.class))}),
    })
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginados(Pageable pageable,
                                                                  @AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam(value = "busca", required = false) String busca) {
        log.info("Endpoint de busca paginada por planos do cliente acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanos(
                pageable,
                ((UserSS) userDetails).getId(),
                busca));
    }


    /**
     * Busca de plano por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de plano por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidPlano   Id do plano a ser buscado
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @GetMapping("/{uuidPlano}")
    @Tag(name = "Busca de plano por id")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um plano pelo id recebido pelo " +
            "parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de plano por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum plano foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PlanoResponse> obtemPlanoPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable(value = "uuidPlano") UUID uuidPlano) {
        log.info("Endpoint de busca de plano por id acessado. ID recebido: {}", uuidPlano);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDePlanoPorId(
                        ((UserSS) userDetails).getId(),
                        uuidPlano));
    }

    /**
     * Busca de dados do plano por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de dados do plano por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidPlano   Id do plano a ser buscado
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @GetMapping("dados/{uuidPlano}")
    @Tag(name = "Busca de dados do plano por id")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca dos dados de um plano pelo id recebido " +
            "por parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca dos dados do plano por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlanoResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum plano foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<DadosPlanoResponse> obtemDadosPlanoPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @PathVariable(value = "uuidPlano") UUID uuidPlano) {
        log.info("Endpoint de busca de dados de plano por id acessado. ID recebido: {}", uuidPlano);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDeDadosDePlanoPorId(
                        ((UserSS) userDetails).getId(),
                        uuidPlano));
    }

    /**
     * Atualiza plano
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de plano por id
     *
     * @param userDetails  Dados do usuário logado na sessão atual
     * @param planoRequest objeto que deve conter todos os dados necessários para atualização do plano
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @PutMapping("/{uuidPlano}")
    @Tag(name = "Atualização de plano de assinatura")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de um plano de assinatura", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plano atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum plano foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PlanoResponse> atualizaPlano(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable(value = "uuidPlano") UUID uuidPlano,
                                                       @Valid @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de atualização de plano acessado");
        return ResponseEntity.ok().body(
                planoService.atualizaPlano(
                        ((UserSS) userDetails).getId(),
                        uuidPlano,
                        planoRequest));
    }

    /**
     * Exclusão de plano
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de plano por id
     *
     * @param userDetails Dados do usuário logado na sessão atual
     * @param uuidPlano   Id do plano a ser removido
     * @return Retorna objeto Plano removido convertido para o tipo PlanoResponse
     */
    @DeleteMapping("/{uuidPlano}")
    @Tag(name = "Remoção de plano por id")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um plano através do id recebido " +
            "pelo parâmetro", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Plano excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum plano foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O plano selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<PlanoResponse> realizaCancelamentoDePlanoPorId(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable(value = "uuidPlano") UUID uuidPlano) {
        log.info("Endpoint de remoção de plano por id acessado. ID recebido: {}", uuidPlano);
        return ResponseEntity.ok().body(planoService
                .cancelaAssinatura(
                        ((UserSS) userDetails).getId(),
                        uuidPlano));
    }
}
