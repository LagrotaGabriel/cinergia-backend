package br.com.backend.modules.cliente.controllers;

import br.com.backend.config.security.JWTUtil;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.dto.response.ClientePageResponse;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.cliente.services.report.ClienteRelatorioService;
import br.com.backend.modules.cliente.services.ClienteService;
import br.com.backend.exceptions.InvalidRequestException;
import br.com.backend.exceptions.ObjectNotFoundException;
import com.lowagie.text.DocumentException;
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

/**
 * ClienteController
 * Esta classe fornece os endpoints para acessar as regras lógicas de negócio referentes à entidade ClienteEntity
 *
 * @author Gabriel Lagrota
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("${default.api.path}/cliente")
@Produces({MediaType.APPLICATION_JSON, "application/json"})
@Consumes({MediaType.APPLICATION_JSON, "application/json"})
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteRelatorioService clienteRelatorioService;

    @Autowired
    JWTUtil jwtUtil;

    /**
     * Obtenção da imagem de perfil do cliente
     * Este método permite que a imagem de perfil do cliente seja obtida através do id do cliente informado
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param id  Chave primária de identificação do cliente
     * @return Retorna cadeia de bytes da imagem de perfil do cliente
     * @throws ObjectNotFoundException Lança exception caso nenhum cliente seja encontrado através do ID informado
     */
    @GetMapping("imagem-perfil/{id}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Obtenção de imagem de perfil do cliente")
    @Operation(summary = "Este endpoint tem como objetivo realizar a obtenção da imagem de perfil de um cliente",
            method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Imagem de perfil do cliente obtida com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<byte[]> obtemImagemDePerfilDoCliente(HttpServletRequest req,
                                                               @PathVariable("id") Long id) {
        log.info("Método controlador de obtenção de imagem de perfil de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.obtemImagemPerfilCliente(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    /**
     * Atualização de imagem de perfil do cliente
     * Este método permite que a imagem de perfil do cliente seja atualizada através do id do cliente informado
     *
     * @param req          Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param imagemPerfil Nova imagem de perfil do cliente a ser atualizada
     * @param id           Chave primária de identificação do cliente
     * @return Retorna objeto Cliente que foi atualizado convertido para o tipo Response
     * @throws ObjectNotFoundException Lança exception caso nenhum cliente seja encontrado através do ID informado
     */
    @PutMapping("imagem-perfil/{id}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Atualização de imagem de perfil do cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização da imagem de perfil de um cliente",
            method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
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

    /**
     * Cadastro de novo cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de novo cliente
     *
     * @param req            Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param clienteRequest Objeto contendo todos os atributos necessários para a criação de um novo cliente
     * @return Retorna objeto Cliente criado convertido para o tipo ClienteResponse
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna na criação do cliente
     * @throws InvalidRequestException Exception lançada caso ocorra alguma falha interna no processo de criação da
     *                                 assinatura do cliente
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Cadastro de novo cliente")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização da imagem de perfil de um cliente",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente salvo com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de criação do cliente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro no processo de criação da assinatura",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<ClienteResponse> criaNovoCliente(HttpServletRequest req,
                                                           @Valid @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de criação de novo cliente acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService
                .criaNovoCliente(jwtUtil.obtemEmpresaAtiva(req), clienteRequest));
    }

    /**
     * Busca paginada de clientes
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de clientes
     *
     * @param busca    Parâmetro opcional. Recebe uma string para busca de clientes por atributos específicos
     * @param pageable Contém especificações da paginação, como tamanho da página, página atual, etc
     * @param req      Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @return Retorna objeto do tipo ClientePageResponse, que possui informações da paginação e a lista de clientes
     * encontrados inserida em seu body
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Busca paginada por clientes cadastrados")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca paginada de clientes cadastrados na empresa " +
            "logada que acionou a requisição com os filtros de busca enviados", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca paginada de clientes foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientePageResponse.class))}),
    })
    public ResponseEntity<ClientePageResponse> obtemClientesPaginados(
            @RequestParam(value = "busca", required = false) String busca,
            Pageable pageable,
            HttpServletRequest req) {
        log.info("Endpoint de busca paginada por clientes acessado. Filtros de busca: {}",
                busca == null ? "Nulo" : busca);
        return ResponseEntity.ok().body(clienteService.realizaBuscaPaginadaPorClientes(
                jwtUtil.obtemEmpresaAtiva(req), pageable, busca));
    }

    /**
     * Validação de duplicidade de CPF/CNPJ
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de verificação de duplicidade
     * no CPF ou CNPJ
     *
     * @param req     Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param cpfCnpj CPF ou CNPJ a ser verificado
     * @return Retorna um ResponseEntity sem body, apenas com o status da requisição
     */
    @PostMapping("/verifica-cpfCnpj")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Validação de duplicidade na entrada do CPF ou CNPJ")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a validação se o CPF ou CNPJ digitado pelo cliente " +
            "já existe na base de dados da empresa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Requisição finalizada com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "O CPF/CNPJ informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))})
    })
    public ResponseEntity<?> verificaDuplicidadeCpfCnpj(HttpServletRequest req,
                                                        @RequestBody String cpfCnpj) {
        log.info("Endpoint de validação de duplicidade de CPF/CNPJ acessado. CPF/CNPJ: " + cpfCnpj);
        clienteService.validaSeCpfCnpjJaExiste(cpfCnpj, jwtUtil.obtemEmpresaAtiva(req).getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Exclusão de cliente em massa
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de clientes em
     * massa
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param ids Lista de ids dos clientes a serem removidos
     * @return Retorna um ResponseEntity sem body, apenas com o status da requisição
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Tag(name = "Remoção de cliente em massa")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de clientes em massa", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Clientes excluídos com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum cliente foi encontrado com os ids informados",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O cliente selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum cliente foi encontrado para remoção",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um cliente que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<?> removeClientesEmMassa(HttpServletRequest req,
                                                   @RequestBody List<Long> ids) {
        log.info("Método controlador de remoção de clientes em massa acessado");
        clienteService.removeClientesEmMassa(jwtUtil.obtemEmpresaAtiva(req), ids);
        return ResponseEntity.ok().build();
    }

    /**
     * Exclusão de cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de cliente por id
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param id  Id do cliente a ser removido
     * @return Retorna objeto Cliente removido convertido para o tipo ClienteResponse
     */
    @DeleteMapping("/{id}")
    @Tag(name = "Remoção de cliente")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um cliente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "O cliente selecionado já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Nenhum cliente foi encontrado para remoção",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Não é possível remover um cliente que já foi excluído",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
    })
    public ResponseEntity<ClienteResponse> removeCliente(HttpServletRequest req,
                                                         @PathVariable Long id) {
        log.info("Método controlador de remoção de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.removeCliente(jwtUtil.obtemEmpresaAtiva(req), id));
    }

    /**
     * Criação de relatório de clientes em PDF
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de relatório de
     * clientes registrados em PDF
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param res Atributo do tipo HttpServletResponse que possui as informações da resposta e deve retornar o arquivo
     *            em PDF com o relatório de clientes cadastrados
     * @param ids Ids dos clientes que deverão ser exibidos no relatório
     * @throws DocumentException Exception lançada caso ocorra um erro na criação do relatório em PDF
     * @throws IOException       Exception lançada caso ocorra um erro na criação do relatório em PDF
     */
    @PostMapping("/relatorio")
    @Tag(name = "Gerar relatório em PDF")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a criação de um relatório em PDF com a listagem de " +
            "clientes solicitada", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "PDF gerado com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Ocorreu um erro na criação do PDF",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Exception.class))}),
    })
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


    /**
     * Busca de cliente por id
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de cliente por id
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param id  Id do cliente a ser buscado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @GetMapping("/{id}")
    @Tag(name = "Busca de cliente por id")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um cliente pelo id recebido pelo " +
            "parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de cliente por id foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> obtemClientePorId(@PathVariable(value = "id") Long id,
                                                             HttpServletRequest req) {
        log.info("Endpoint de busca de cliente por id acessado. ID recebido: {}", id);
        return ResponseEntity.ok().body(clienteService
                .realizaBuscaDeClientePorId(jwtUtil.obtemEmpresaAtiva(req), id));
    }


    /**
     * Atualiza cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de cliente por id
     *
     * @param req Atributo do tipo HttpServletRequest que possui as informações da requisição
     * @param clienteRequest objeto que deve conter todos os dados necessários para atualização do cliente
     * @param id  Id do cliente a ser atualizado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @PutMapping("/{id}")
    @Tag(name = "Atualização de cliente")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a atualização de um cliente na base de dados da " +
            "empresa", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "400", description = "A inscrição estadual informada já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "400", description = "O CPF/CNPJ informado já existe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvalidRequestException.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente foi encontrado com o id informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> atualizaCliente(HttpServletRequest req,
                                                           @RequestBody ClienteRequest clienteRequest,
                                                           @PathVariable Long id) {
        log.info("Método controlador de atualização de cliente acessado");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.atualizaCliente(jwtUtil.obtemEmpresaAtiva(req), id, clienteRequest));
    }
}
