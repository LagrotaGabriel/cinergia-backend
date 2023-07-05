package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.models.dto.empresa.response.DadosDashBoardResponse;
import br.com.backend.models.dto.empresa.response.DadosGraficoResponse;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.dto.empresa.response.EmpresaSimplificadaResponse;
import br.com.backend.services.empresa.EmpresaService;
import br.com.backend.services.exceptions.InvalidRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints de CRUD da entidade Empresa")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/empresa")
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping("/dashboard")
    @ApiOperation(
            value = "Obtenção de dados de dashboard da empresa",
            notes = "Esse endpoint tem como objetivo realizar a obtenção de dados estatísticos da empresa atual",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Obtenção dos dados da empresa realizada com sucesso", response = EmpresaResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de obtenção dos dados da empresa",
                    response = InvalidRequestException.class),
    })
    public ResponseEntity<DadosDashBoardResponse> obtemDadosEstatisticosEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados estatísticos da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosDashBoardEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    @GetMapping("/grafico-faturamento")
    @ApiOperation(
            value = "Obtenção de dados do gráfico de faturamento da empresa",
            notes = "Esse endpoint tem como objetivo realizar a obtenção de dados do gráfico de faturamento da empresa atual",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Obtenção dos dados gráficos da empresa realizada com sucesso", response = EmpresaResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de obtenção dos dados gráficos da empresa",
                    response = InvalidRequestException.class),
    })
    public ResponseEntity<Map<Integer, DadosGraficoResponse>> obtemDadosGraficoFaturamentoEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados do gráfico de faturamento da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosGraficoFaturamentoEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    @GetMapping("/simplificado")
    @ApiOperation(
            value = "Obtenção simplificada de dados da empresa",
            notes = "Esse endpoint tem como objetivo realizar a obtenção do nome e saldo da empresa logada",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Obtenção dos dados da empresa realizada com sucesso", response = EmpresaResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de obtenção dos dados da empresa",
                    response = InvalidRequestException.class),
    })
    public ResponseEntity<EmpresaSimplificadaResponse> obtemDadosSimplificadosEmpresa(HttpServletRequest req) {
        log.info("Método controlador de obtenção de dados simplificados da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(empresaService.obtemDadosSimplificadosEmpresa(jwtUtil.obtemEmpresaAtiva(req)));
    }

    @PostMapping
    @ApiOperation(
            value = "Cadastro de nova empresa",
            notes = "Esse endpoint tem como objetivo realizar o cadastro de uma nova empresa no banco de dados do projeto",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Empresa salva com sucesso", response = EmpresaResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de criação da empresa", response = InvalidRequestException.class),
    })
    public ResponseEntity<EmpresaResponse> criaNovaEmpresa(@Valid @RequestBody EmpresaRequest empresaRequest) {
        log.info("Método controlador de criação de nova empresa acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.criaNovaEmpresa(empresaRequest));
    }

}
