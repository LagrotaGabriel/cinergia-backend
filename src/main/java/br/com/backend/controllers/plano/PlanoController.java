package br.com.backend.controllers.plano;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.cliente.response.ClienteResponse;
import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.dto.plano.response.DadosPlanoResponse;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.services.exceptions.ObjectNotFoundException;
import br.com.backend.services.plano.PlanoService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/plano")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class PlanoController {

    @Autowired
    PlanoService planoService;

    @Autowired
    JWTUtil jwtUtil;


    /**
     * Criação de novo plano
     * Este método permite que o acionamento lógico do método de criação de novo plano de assinatura para um cliente
     * seja acionado
     *
     * @param id           Chave primária de identificação do cliente
     * @param req          Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param planoRequest Objeto que deve conter todos os atributos necessários para a criação de um novo plano
     * @return Objeto Plano criado convertido para objeto do tipo response
     */
    @PostMapping("/{id}")
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
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<PlanoResponse> criaNovoPlano(
            @PathVariable(value = "id") Long id,
            HttpServletRequest req,
            @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de criação de novo plano acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(planoService.criaNovoPlano(
                jwtUtil.obtemEmpresaAtiva(req), id, planoRequest));
    }


    /**
     * Busca paginada de planos do cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de planos
     * relacionados com o cliente encontrado através do id informado via parâmetro
     *
     * @param idCliente Deve ser utilizado para identificar um cliente e localizar os planos cadastrados em seu perfil
     * @param pageable  Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param req       Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @return Retorna objeto do tipo PlanoPageResponse, que possui informações da paginação e a lista de planos
     * encontrados inserida em seu body
     */
    @GetMapping("/cliente/{idCliente}")
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
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginadosDoCliente(
            @PathVariable(value = "idCliente") Long idCliente,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por planos acessado");
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanosDoCliente(
                jwtUtil.obtemEmpresaAtiva(req), pageable, idCliente));
    }


    /**
     * Busca paginada de planos
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de planos
     * cadastrados
     *
     * @param busca    Parâmetro opcional. Recebe uma string para busca de planos por atributos específicos
     * @param pageable Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param req      Atributo do tipo HttpServletRequest que possui as informações da requisição
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
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginados(
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por planos do cliente acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanos(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }


    /**
     * Busca de plano por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de plano por id
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param id  Id do plano a ser buscado
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @GetMapping("/{id}")
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
    public ResponseEntity<PlanoResponse> obtemPlanoPorId(@PathVariable(value = "id") Long id,
                                                         HttpServletRequest req) {
        log.info("Endpoint de busca de plano por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDePlanoPorId(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    /**
     * Busca de dados do plano por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de dados do plano por id
     *
     * @param req     Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param idPlano Id do plano a ser buscado
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @GetMapping("dados/{idPlano}")
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
    public ResponseEntity<DadosPlanoResponse> obtemDadosPlanoPorId(@PathVariable(value = "idPlano") Long idPlano,
                                                                   HttpServletRequest req) {
        log.info("Endpoint de busca de dados de plano por id acessado. ID recebido: {}", idPlano);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDeDadosDePlanoPorId(jwtUtil.obtemEmpresaAtiva(req), idPlano));
    }

    /**
     * Atualiza plano
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de plano por id
     *
     * @param req          Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param planoRequest objeto que deve conter todos os dados necessários para atualização do plano
     * @return Retorna objeto Plano encontrado convertido para o tipo PlanoResponse
     */
    @PutMapping("/{id}")
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
    public ResponseEntity<PlanoResponse> atualizaPlano(
            HttpServletRequest req,
            @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de atualização de plano acessado");
        return ResponseEntity.ok().body(planoService.atualizaPlano(
                jwtUtil.obtemEmpresaAtiva(req), planoRequest));
    }

    /**
     * Exclusão de plano
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de plano por id
     *
     * @param req     Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param idPlano Id do plano a ser removido
     * @return Retorna objeto Plano removido convertido para o tipo PlanoResponse
     */
    @DeleteMapping("/{idPlano}")
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
    public ResponseEntity<PlanoResponse> realizaCancelamentoDePlanoPorId(@PathVariable(value = "idPlano") Long idPlano,
                                                                         HttpServletRequest req) {
        log.info("Endpoint de remoção de plano por id acessado. ID recebido: {}", idPlano);
        return ResponseEntity.ok().body(planoService.cancelaAssinatura(idPlano, jwtUtil.obtemEmpresaAtiva(req)));
    }
}
