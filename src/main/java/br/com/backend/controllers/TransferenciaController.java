package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.dto.transferencia.request.TransferenciaRequest;
import br.com.backend.models.dto.transferencia.response.TransferenciaPageResponse;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.services.transferencia.TransferenciaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Esta API disponibiliza os endpoints de CRUD da entidade Transferencia")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/transferencia")
public class TransferenciaController {

    @Autowired
    TransferenciaService transferenciaService;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping()
    @ApiOperation(
            value = "Criação de nova transferência via PIX",
            notes = "Esse endpoint tem como objetivo realizar a criação de uma transferência via PIX",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A criação da transferência foi realizada com sucesso",
                    response = TransferenciaPageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<?> criaNovaTransferencia(
            HttpServletRequest req,
            @RequestBody TransferenciaRequest transferenciaRequest) {
        log.info("Endpoint de criação de novo plano acessado");
        transferenciaService.criaTransferencia(jwtUtil.obtemEmpresaAtiva(req), transferenciaRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(
            value = "Obtenção de transferencias de uma empresa",
            notes = "Esse endpoint tem como objetivo realizar a obtenção de transferências de uma empresa",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Obtenção das transferências da empresa realizada com sucesso",
                    response = EmpresaResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de obtenção das transferências da empresa",
                    response = InvalidRequestException.class),
    })
    public ResponseEntity<TransferenciaPageResponse> obtemTransferenciasEmpresa(HttpServletRequest req,
                                                                                Pageable pageable) {
        log.info("Método controlador de obtenção de transferências da empresa acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transferenciaService.obtemTransferenciasEmpresa(jwtUtil.obtemEmpresaAtiva(req), pageable));
    }

}
