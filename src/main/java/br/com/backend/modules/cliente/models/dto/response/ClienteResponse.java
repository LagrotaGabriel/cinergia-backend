package br.com.backend.modules.cliente.models.dto.response;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
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
    private String observacoes;
    private StatusClienteEnum statusCliente;
    private String dataNascimento;
    private TipoPessoaEnum tipoPessoa;
    private AcessoSistemaEntity acessoSistema;
    private EnderecoEntity endereco;
    private ImagemEntity fotoPerfil;
    private List<TelefoneEntity> telefones;

}
