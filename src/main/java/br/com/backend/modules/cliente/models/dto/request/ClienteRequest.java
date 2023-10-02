package br.com.backend.modules.cliente.models.dto.request;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import javax.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {
    private Long id;
    @NotNull
    private String nome;
    private String email;
    private String cpfCnpj;
    private String observacoes;
    @NotNull
    private StatusClienteEnum statusCliente;
    private String dataNascimento;
    @NotNull
    private TipoPessoaEnum tipoPessoa;
    private AcessoSistemaEntity acessoSistema;
    private EnderecoEntity endereco;
    private List<TelefoneEntity> telefones;

}
