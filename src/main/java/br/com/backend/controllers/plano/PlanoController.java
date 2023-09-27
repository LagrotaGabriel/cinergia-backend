package br.com.backend.controllers.plano;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.dto.plano.response.DadosPlanoResponse;
import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.dto.plano.response.PlanoResponse;
import br.com.backend.services.exceptions.ObjectNotFoundException;
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

    @PostMapping("/{id}")
    @ApiOperation(
            value = "Criação de um novo plano para um determinado cliente",
            notes = "Esse endpoint tem como objetivo realizar a criação de um novo plano de assinatura para um determinado " +
                    "cliente",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A criação de um novo plano foi realizada com sucesso",
                    response = PlanoPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PlanoResponse> criaNovoPlano(
            @PathVariable(value = "id") Long id,
            HttpServletRequest req,
            @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de criação de novo plano acessado");
        return ResponseEntity.ok().body(planoService.criaNovoPlano(
                jwtUtil.obtemEmpresaAtiva(req), id, planoRequest));
    }

    @GetMapping("/cliente/{id}")
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
    public ResponseEntity<PlanoPageResponse> obtemPlanosPaginadosDoCliente(
            @PathVariable(value = "id") Long id,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por planos acessado");
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanosDoCliente(
                jwtUtil.obtemEmpresaAtiva(req), pageable, id));
    }

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
        log.info("Endpoint de busca paginada por planos do cliente acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(planoService.realizaBuscaPaginadaPorPlanos(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Busca de plano por id",
            notes = "Esse endpoint tem como objetivo realizar a busca de um plano pelo id recebido pelo parâmetro",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca de plano por id foi realizada com sucesso",
                    response = PlanoResponse.class),
            @ApiResponse(code = 400, message = "Nenhum plano foi encontrado com o id informado",
                    response = ObjectNotFoundException.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PlanoResponse> obtemPlanoPorId(@PathVariable(value = "id") Long id,
                                                         HttpServletRequest req) {
        log.info("Endpoint de busca de plano por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDePlanoPorId(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    @GetMapping("dados/{id}")
    @ApiOperation(
            value = "Busca de plano por id",
            notes = "Esse endpoint tem como objetivo realizar a busca dos dados de um plano pelo id recebido pelo parâmetro",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca dos dados de um plano por id foi realizada com sucesso",
                    response = PlanoResponse.class),
            @ApiResponse(code = 400, message = "Nenhum plano foi encontrado com o id informado",
                    response = ObjectNotFoundException.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<DadosPlanoResponse> obtemDadosPlanoPorId(@PathVariable(value = "id") Long id,
                                                                   HttpServletRequest req) {
        log.info("Endpoint de busca de dados de plano por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(planoService
                .realizaBuscaDeDadosDePlanoPorId(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    @PostMapping("/{id}")
    @ApiOperation(
            value = "Atualização de plano de assinatura",
            notes = "Esse endpoint tem como objetivo realizar a atualização de um novo plano de assinatura",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A atualização de um plano foi realizada com sucesso",
                    response = PlanoResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PlanoResponse> atualizaPlano(
            HttpServletRequest req,
            @RequestBody PlanoRequest planoRequest) {
        log.info("Endpoint de atualização de plano acessado");
        return ResponseEntity.ok().body(planoService.atualizaPlano(
                jwtUtil.obtemEmpresaAtiva(req), planoRequest));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Remoção de plano por id",
            notes = "Esse endpoint tem como objetivo realizar a remoção de um plano pelo id recebido pelo parâmetro",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A remoção de um plano por id foi realizada com sucesso",
                    response = PlanoResponse.class),
            @ApiResponse(code = 400, message = "Nenhum plano foi encontrado com o id informado",
                    response = ObjectNotFoundException.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<PlanoResponse> realizaCancelamentoDePlanoPorId(@PathVariable(value = "id") Long id,
                                                                         HttpServletRequest req) {
        log.info("Endpoint de remoção de plano por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(planoService.cancelaAssinatura(id, jwtUtil.obtemEmpresaAtiva(req)));
    }
}
