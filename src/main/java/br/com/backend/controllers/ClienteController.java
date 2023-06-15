package br.com.backend.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.models.dto.cliente.request.ClienteRequest;
import br.com.backend.models.dto.cliente.response.ClientePageResponse;
import br.com.backend.models.dto.cliente.response.ClienteResponse;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.services.cliente.ClienteRelatorioService;
import br.com.backend.services.cliente.ClienteService;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.services.exceptions.ObjectNotFoundException;
import com.lowagie.text.DocumentException;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(value = "Esta API disponibiliza os endpoints de CRUD da entidade Cliente")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
@RequestMapping("api/v1/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteRelatorioService clienteRelatorioService;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping("imagem-perfil/{id}")
    @ApiOperation(
            value = "Obtenção de imagem de perfil do cliente",
            notes = "Esse endpoint tem como objetivo realizar a obtenção da imagem de perfil de um cliente",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Imagem de perfil do cliente obtida com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado", response = ObjectNotFoundException.class)
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<byte[]> obtemImagemDePerfilDoCliente(HttpServletRequest req,
                                                               @PathVariable("id") Long id) {
        log.info("Método controlador de obtenção de imagem de perfil de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.obtemImagemPerfilCliente(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    @PutMapping("imagem-perfil/{id}")
    @ApiOperation(
            value = "Atualização de imagem de perfil do cliente",
            notes = "Esse endpoint tem como objetivo realizar a atualização da imagem de perfil de um cliente",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado", response = ObjectNotFoundException.class)
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClienteResponse> atualizaImagemPerfilCliente(HttpServletRequest req,
                                                                       @RequestParam(value = "imagemPerfil", required = false) MultipartFile imagemPerfil,
                                                                       @PathVariable("id") Long id) throws IOException {
        log.info("Método controlador de atualização de imagem de perfil de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.atualizaImagemPerfilCliente(
                        jwtUtil.obtemEmpresaAtiva(req),
                        id,
                        imagemPerfil));
    }


    @PostMapping
    @ApiOperation(
            value = "Cadastro de novo cliente",
            notes = "Esse endpoint tem como objetivo realizar o cadastro de um novo cliente no banco de dados do projeto " +
                    "e na integradora de pagamentos ASAAS",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente salvo com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de criação do cliente", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Ocorreu um erro no processo de criação da assinatura", response = InvalidRequestException.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClienteResponse> criaNovoCliente(HttpServletRequest req,
                                                           @Valid @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de criação de novo cliente acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService
                .criaNovoCliente(jwtUtil.obtemEmpresaAtiva(req), clienteRequest));
    }

    @GetMapping
    @ApiOperation(
            value = "Busca paginada por clientes cadastrados",
            notes = "Esse endpoint tem como objetivo realizar a busca paginada de clientes cadastrados na empresa " +
                    "logada que acionou a requisição com os filtros de busca enviados",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca paginada de clientes foi realizada com sucesso",
                    response = ClientePageResponse.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClientePageResponse> obtemClientesPaginados(
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por clientes acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(clienteService.realizaBuscaPaginadaPorClientes(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }

    @PostMapping("/verifica-cpfCnpj")
    @ApiOperation(
            value = "Validação de duplicidade na entrada do CPF ou CNPJ",
            notes = "Esse endpoint tem como objetivo realizar a validação se o CPF ou CNPJ digitado pelo cliente já " +
                    "existe na base de dados da empresa.",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Requisição finalizada com sucesso"),
            @ApiResponse(code = 400, message = "O CPF/CNPJ informado já existe",
                    response = InvalidRequestException.class)
    })
    @Produces({MediaType.APPLICATION_JSON, "application/json"})
    @Consumes({MediaType.APPLICATION_JSON, "application/json"})
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<?> verificaDuplicidadeCpfCnpj(HttpServletRequest req,
                                                        @RequestBody String cpfCnpj) {
        log.info("Endpoint de validação de duplicidade de CPF/CNPJ acessado. CPF/CNPJ: " + cpfCnpj);
        clienteService.validaSeCpfCnpjJaExiste(cpfCnpj, jwtUtil.obtemEmpresaAtiva(req).getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ApiOperation(
            value = "Remoção de cliente em massa",
            notes = "Esse endpoint tem como objetivo realizar a exclusão de clientes em massa",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Clientes excluídos com sucesso"),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado", response = ObjectNotFoundException.class),
            @ApiResponse(code = 400, message = "O cliente selecionado já foi excluído", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado para remoção", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Não é possível remover um cliente que já foi excluído", response = InvalidRequestException.class)
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<?> removeClientesEmMassa(HttpServletRequest req,
                                                   @RequestBody List<Long> ids) {
        log.info("Método controlador de remoção de clientes em massa acessado");
        clienteService.removeClientesEmMassa(jwtUtil.obtemEmpresaAtiva(req), ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Remoção de cliente",
            notes = "Esse endpoint tem como objetivo realizar a exclusão de um cliente",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Clientes excluídos com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado", response = ObjectNotFoundException.class),
            @ApiResponse(code = 400, message = "O cliente selecionado já foi excluído", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado para remoção", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Não é possível remover um cliente que já foi excluído", response = InvalidRequestException.class)
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClienteResponse> removeCliente(HttpServletRequest req,
                                                         @PathVariable Long id) {
        log.info("Método controlador de remoção de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.removeCliente(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    @PostMapping("/relatorio")
    @ApiOperation(
            value = "Gerar relatório em PDF",
            notes = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de clientes " +
                    "solicitada",
            produces = MediaType.APPLICATION_OCTET_STREAM,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "PDF gerado com sucesso"),
            @ApiResponse(code = 400, message = "Ocorreu um erro na criação do PDF", response = Exception.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public void relatorio(HttpServletResponse res,
                          HttpServletRequest req,
                          @RequestBody List<Long> ids) throws DocumentException, IOException {
        log.info("Método controlador de obtenção de relatório de clientes em PDF acessado. IDs: {}", ids);

        EmpresaEntity empresaLogada = jwtUtil.obtemEmpresaAtiva(req);

        res.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; relatorio"
                + "_clientes_"
                + new SimpleDateFormat("dd.MM.yyyy_HHmmss").format(new Date())
                + ".pdf";
        res.setHeader(headerKey, headerValue);

        clienteRelatorioService.exportarPdf(res, empresaLogada, ids);
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Busca de cliente por id",
            notes = "Esse endpoint tem como objetivo realizar a busca de um cliente pelo id recebido pelo parâmetro",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "A busca de cliente por id foi realizada com sucesso",
                    response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado",
                    response = ObjectNotFoundException.class),
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClienteResponse> obtemClientePorId(@PathVariable(value = "id") Long id,
                                                             HttpServletRequest req) {
        log.info("Endpoint de busca de cliente por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(clienteService
                .realizaBuscaDeClientePorId(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    @PutMapping("/{id}")
    @ApiOperation(
            value = "Atualização de cliente",
            notes = "Esse endpoint tem como objetivo realizar a atualização de um cliente na base de dados da empresa",
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "A inscrição estadual informada já existe", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "O CPF/CNPJ informado já existe", response = InvalidRequestException.class),
            @ApiResponse(code = 400, message = "Nenhum cliente foi encontrado com o id informado", response = ObjectNotFoundException.class)
    })
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    public ResponseEntity<ClienteResponse> atualizaCliente(HttpServletRequest req,
                                                           @RequestBody ClienteRequest clienteRequest,
                                                           @PathVariable Long id) {
        log.info("Método controlador de atualização de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.atualizaCliente(jwtUtil.obtemEmpresaAtiva(req), id, clienteRequest));
    }


}
