package br.com.backend.modules.empresa.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import br.com.backend.modules.empresa.models.dto.response.DadosDashBoardResponse;
import br.com.backend.modules.empresa.models.dto.response.DadosGraficoResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaSimplificadaResponse;
import br.com.backend.modules.empresa.services.EmpresaService;
import br.com.backend.exceptions.custom.InvalidRequestException;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * EmpresaController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade EmpresaEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/empresa")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;

    @Autowired
    JWTUtil jwtUtil;

    /**
     * Obtenção dos dados de dashboard da empresa
     * Este método permite que os dados necessários para a criação de um dashboard da empresa sejam disponibilizados
     * para o servidor client
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @return Retorna dados do dashboard da empresa, com informações que deverão ser exibidas de forma gráfica
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Obtenção de dados de dashboard da empresa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção de dados estatísticos da empresa atual",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Obtenção dos dados da empresa realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpresaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de obtenção dos dados da empresa",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<DadosDashBoardResponse> obtemDadosEstatisticosEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados estatísticos da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosDashBoardEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    /**
     * Obtenção dos dados de faturamento da empresa
     * Este método tem como objetivo retornar os dados de faturamento da empresa responsável pelo envio da requisição
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @return Retorna dados de faturamento da empresa
     */
    @GetMapping("/grafico-faturamento")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Obtenção de dados do gráfico de faturamento da empresa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção de dados do gráfico de faturamento da " +
            "empresa atual", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Obtenção dos dados de faturamento da empresa realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpresaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de obtenção dos dados de faturamento da empresa",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<Map<Integer, DadosGraficoResponse>> obtemDadosGraficoFaturamentoEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados do gráfico de faturamento da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosGraficoFaturamentoEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    /**
     * Obtenção simplificada dos dados da empresa
     * Este método tem como objetivo retornar dados resumidos sobre a empresa
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @return Retorna dados simplificados da empresa
     */
    @GetMapping("/simplificado")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Obtenção simplificada de dados da empresa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a obtenção do nome e saldo da empresa logada",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Obtenção dos dados simplificados da empresa realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpresaResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de obtenção dos dados simplificados da empresa",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<EmpresaSimplificadaResponse> obtemDadosSimplificadosEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados simplificados da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosSimplificadosEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    /**
     * Criação de nova empresa
     * Este método tem como objetivo disponibilizar o acionamento lógico de criação de nova empresa
     *
     * @param empresaRequest Objeto contendo todos os atributos necessários para a criação de nova empresa
     * @return Retorna empresa criada convertida para objeto do tipo Response
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Cadastro de nova empresa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar o cadastro de uma nova empresa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa persistida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpresaResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Ocorreu um erro no processo de criação da empresa",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<EmpresaResponse> criaNovaEmpresa(@Valid @RequestBody EmpresaRequest empresaRequest) {
        log.info("Método controlador de criação de nova empresa acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.criaNovaEmpresa(empresaRequest));
    }

}
