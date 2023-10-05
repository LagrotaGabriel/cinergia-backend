package br.com.backend.modules.cliente.controllers;

import br.com.backend.config.security.UserSS;
import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.dto.response.ClienteResponse;
import br.com.backend.modules.cliente.models.dto.response.page.ClientePageResponse;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.services.ClienteService;
import br.com.backend.modules.cliente.services.report.ClienteRelatorioService;
import br.com.backend.modules.cliente.services.validator.ClienteValidationService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    //TODO ATUALIZAR DOCUMENTAÇÃO COM ACTIVE USERS

    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteValidationService clienteValidationService;

    @Autowired
    ClienteRelatorioService clienteRelatorioService;

    /**
     * Cadastro de novo cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de novo cliente
     *
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
    public ResponseEntity<ClienteResponse> criaNovoCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                           @Valid @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de criação de novo cliente acessado");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clienteService.criaNovoCliente(
                        ((UserSS) userDetails).getId(),
                        clienteRequest));
    }

    /**
     * Busca paginada de clientes
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca paginada de clientes
     *
     * @param busca    Parâmetro opcional. Recebe uma string para busca de clientes por atributos específicos
     * @param pageable Contém especificações da paginação, como tamanho da página, página atual, etc
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
    public ResponseEntity<ClientePageResponse> obtemClientesPaginados(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @RequestParam(value = "busca", required = false) String busca,
                                                                      Pageable pageable) {
        log.info("Endpoint de busca paginada por clientes acessado. Filtros de busca: {}", busca);
        return ResponseEntity.ok().body(
                clienteService.realizaBuscaPaginadaPorClientes(
                        ((UserSS) userDetails).getId(),
                        pageable,
                        busca));
    }

    /**
     * Busca de cliente por uuidCliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de busca de cliente por uuidCliente
     *
     * @param uuidCliente Id do cliente a ser buscado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @GetMapping("/{uuidCliente}")
    @Tag(name = "Busca de cliente por uuidCliente")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a busca de um cliente pelo uuidCliente recebido pelo " +
            "parâmetro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "A busca de cliente por uuidCliente foi realizada com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponse.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o uuidCliente informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> obtemClientePorId(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable(value = "uuidCliente") UUID uuidCliente) {
        log.info("Endpoint de busca de cliente por uuidCliente acessado. ID recebido: {}", uuidCliente);
        return ResponseEntity.ok().body(
                clienteService.realizaBuscaDeClientePorId(
                        new ClienteId(((UserSS) userDetails).getId(), uuidCliente)));
    }

    /**
     * Obtenção da imagem de perfil do cliente
     * Este método permite que a imagem de perfil do cliente seja obtida através do uuidCliente do cliente informado
     *
     * @param uuidCliente Chave primária de identificação do cliente
     * @return Retorna cadeia de bytes da imagem de perfil do cliente
     * @throws ObjectNotFoundException Lança exception caso nenhum cliente seja encontrado através do ID informado
     */
    @GetMapping("imagem-perfil/{uuidCliente}")
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
                    description = "Nenhum cliente foi encontrado com o uuidCliente informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<byte[]> obtemImagemDePerfilDoCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable("uuidCliente") UUID uuidCliente) {
        log.info("Método controlador de obtenção de imagem de perfil de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.obtemImagemPerfilCliente(
                        new ClienteId(((UserSS) userDetails).getId(), uuidCliente)));
    }

    /**
     * Atualiza cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de atualização de cliente por uuidCliente
     *
     * @param clienteRequest objeto que deve conter todos os dados necessários para atualização do cliente
     * @param uuidCliente    Id do cliente a ser atualizado
     * @return Retorna objeto Cliente encontrado convertido para o tipo ClienteResponse
     */
    @PutMapping("/{uuidCliente}")
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
            @ApiResponse(responseCode = "404", description = "Nenhum cliente foi encontrado com o uuidCliente informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> atualizaCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable UUID uuidCliente,
                                                           @RequestBody ClienteRequest clienteRequest) {
        log.info("Método controlador de atualização de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.atualizaCliente(
                        new ClienteId(((UserSS) userDetails).getId(), uuidCliente),
                        clienteRequest));
    }

    /**
     * Atualização de imagem de perfil do cliente
     * Este método permite que a imagem de perfil do cliente seja atualizada através do uuidCliente do cliente informado
     *
     * @param imagemPerfil Nova imagem de perfil do cliente a ser atualizada
     * @param uuidCliente  Chave primária de identificação do cliente
     * @return Retorna objeto Cliente que foi atualizado convertido para o tipo Response
     * @throws ObjectNotFoundException Lança exception caso nenhum cliente seja encontrado através do ID informado
     */
    @PutMapping("imagem-perfil/{uuidCliente}")
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
                    description = "Nenhum cliente foi encontrado com o uuidCliente informado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectNotFoundException.class))})
    })
    public ResponseEntity<ClienteResponse> atualizaImagemPerfilCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                                       @RequestParam(value = "imagemPerfil", required = false) MultipartFile imagemPerfil,
                                                                       @PathVariable("uuid") UUID uuidCliente) throws IOException {
        log.info("Método controlador de atualização de imagem de perfil de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.atualizaImagemPerfilCliente(
                        new ClienteId(((UserSS) userDetails).getId(), uuidCliente),
                        imagemPerfil));
    }

    /**
     * Exclusão de cliente
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de exclusão de cliente por uuid
     *
     * @param uuidCliente Id do cliente a ser removido
     * @return Retorna objeto Cliente removido convertido para o tipo ClienteResponse
     */
    @DeleteMapping("/{uuidCliente}")
    @Tag(name = "Remoção de cliente")
    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @Operation(summary = "Esse endpoint tem como objetivo realizar a exclusão de um cliente", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum cliente foi encontrado com o uuid informado",
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
    public ResponseEntity<ClienteResponse> removeCliente(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable UUID uuidCliente) {
        log.info("Método controlador de remoção de cliente acessado");
        return ResponseEntity.status(HttpStatus.OK).body(
                clienteService.removeCliente(new ClienteId(((UserSS) userDetails).getId(), uuidCliente)));
    }

    /**
     * Validação de duplicidade de CPF/CNPJ
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de verificação de duplicidade
     * no CPF ou CNPJ
     *
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
    public ResponseEntity<?> verificaDuplicidadeCpfCnpj(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody String cpfCnpj) {
        log.info("Endpoint de validação de duplicidade de CPF/CNPJ acessado. CPF/CNPJ: " + cpfCnpj);
        clienteValidationService.validaSeCpfCnpjJaExiste(
                ((UserSS) userDetails).getId(),
                cpfCnpj
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Criação de relatório de clientes em PDF
     * Este método tem como objetivo disponibilizar o endpoint de acionamento da lógica de criação de relatório de
     * clientes registrados em PDF
     *
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
    public void relatorio(@AuthenticationPrincipal UserDetails userDetails,
                          HttpServletResponse res,
                          @RequestBody List<UUID> ids) throws DocumentException, IOException {
        log.info("Método controlador de obtenção de relatório de clientes em PDF acessado. IDs: {}", ids);

        // TODO CRIAR UTIL PARA ABSTRAIR INFORMAÇÕES ABAIXO

        res.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; relatorio"
                + "_clientes_"
                + new SimpleDateFormat("dd.MM.yyyy_HHmmss").format(new Date())
                + ".pdf";
        res.setHeader(headerKey, headerValue);

        clienteRelatorioService.exportarPdf(
                ((UserSS) userDetails).getId(), res, ids);
    }
}
