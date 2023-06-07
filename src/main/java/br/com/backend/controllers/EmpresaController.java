package br.com.backend.controllers;

import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
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

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
