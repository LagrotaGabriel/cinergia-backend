package br.com.backend.modules.cliente.models.dto.request;

import br.com.backend.globals.models.acesso.request.AcessoSistemaRequest;
import br.com.backend.globals.models.endereco.dto.request.EnderecoRequest;
import br.com.backend.globals.models.telefone.request.TelefoneRequest;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotEmpty(message = "O nome do cliente não pode estar vazio")
    @Size(max = 70, message = "O nome do cliente deve conter no máximo {max} caracteres")
    private String nome;

    @Email(message = "O padrão do campo e-mail é inválido")
    @Size(max = 70, message = "O e-mail do cliente deve conter no máximo {max} caracteres")
    private String email;

    @NotEmpty(message = "O campo Cpf/Cnpj do cliente não pode estar vazio")
    @Pattern(regexp = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})|(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})",
            message = "O padrão do campo Cpf/Cnpj é inválido")
    @Size(message = "O campo Cpf/Cnpj do cliente deve ter no máximo {max} caracteres", max = 18)
    private String cpfCnpj;

    @Size(message = "O campo observações deve conter no máximo {max} caracteres", max = 300)
    private String observacoes;

    @NotNull(message = "O status do cliente não pode ser nulo")
    private StatusClienteEnum statusCliente;

    @Pattern(regexp = "([0-2]\\d|3[0-1])/(0\\d|1[0-2])/((19|20)\\d\\d)",
            message = "O padrão do campo data de nascimento está inválido")
    private String dataNascimento;

    @NotNull(message = "O campo tipo pessoa não pode ser nulo")
    private TipoPessoaEnum tipoPessoa;

    @Valid
    private AcessoSistemaRequest acessoSistema;

    @Valid
    private EnderecoRequest endereco;

    @Valid
    private List<TelefoneRequest> telefones;

}
