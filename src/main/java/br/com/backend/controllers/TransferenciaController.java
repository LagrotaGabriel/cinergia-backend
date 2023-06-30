package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
