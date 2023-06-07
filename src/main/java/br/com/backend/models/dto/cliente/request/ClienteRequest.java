package br.com.backend.models.dto.cliente.request;

import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import br.com.backend.models.enums.StatusClienteEnum;
import br.com.backend.models.enums.TipoPessoaEnum;
import javax.validation.constraints.NotNull;
import lombok.*;

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
    @NotNull
    private StatusClienteEnum statusCliente;
    private String dataNascimento;

    @NotNull
    private TipoPessoaEnum tipoPessoa;
    private TelefoneEntity telefone;
    private EnderecoEntity endereco;

}
