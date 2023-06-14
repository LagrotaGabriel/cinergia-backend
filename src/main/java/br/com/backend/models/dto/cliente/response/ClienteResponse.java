package br.com.backend.models.dto.cliente.response;

import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.ImagemEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import br.com.backend.models.enums.StatusClienteEnum;
import br.com.backend.models.enums.TipoPessoaEnum;
import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String dataCadastro;
    private String horaCadastro;
    private String nome;
    private String email;
    private String cpfCnpj;
    private StatusClienteEnum statusCliente;
    private String dataNascimento;
    private TipoPessoaEnum tipoPessoa;
    private AcessoSistemaEntity acessoSistema;
    private EnderecoEntity endereco;
    private ImagemEntity fotoPerfil;
    private List<TelefoneEntity> telefones;

}
