package br.com.backend.modules.cliente.models.dto.response;

import br.com.backend.globals.models.endereco.dto.response.EnderecoResponse;
import br.com.backend.globals.models.telefone.response.TelefoneResponse;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private UUID uuid;
    private String dataCadastro;
    private String horaCadastro;
    private String nome;
    private String email;
    private String cpfCnpj;
    private String observacoes;
    private String statusCliente;
    private String dataNascimento;
    private String tipoPessoa;
    private EnderecoResponse endereco;
    private List<TelefoneResponse> telefones;

    public ClienteResponse constroiClienteResponse(ClienteEntity clienteEntity) {
        log.info("Método de conversão de objeto do tipo ClienteEntity para objeto do tipo ClienteResponse acessado");

        log.info("Iniciando construção do objeto ClienteResponse...");
        ClienteResponse clienteResponse = ClienteResponse.builder()
                .uuid(clienteEntity.getUuid())
                .dataCadastro(clienteEntity.getDataCriacao())
                .horaCadastro(clienteEntity.getHoraCriacao())
                .nome(clienteEntity.getNome())
                .email(clienteEntity.getEmail())
                .cpfCnpj(clienteEntity.getCpfCnpj())
                .observacoes(clienteEntity.getObservacoes())
                .statusCliente(clienteEntity.getStatusCliente().toString())
                .dataNascimento(clienteEntity.getDataNascimento())
                .tipoPessoa(clienteEntity.getTipoPessoa().toString())
                .endereco(clienteEntity.getEndereco() == null
                        ? null
                        : new EnderecoResponse().constroiEnderecoResponse(clienteEntity.getEndereco()))
                .telefones(clienteEntity.getTelefones() == null
                        ? new ArrayList<>()
                        : new TelefoneResponse().constroiListaTelefoneResponse(clienteEntity.getTelefones()))
                .build();
        log.debug("Objeto ClienteResponse buildado com sucesso. Retornando...");
        return clienteResponse;
    }

}
