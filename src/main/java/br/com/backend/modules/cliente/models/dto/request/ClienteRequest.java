package br.com.backend.modules.cliente.models.dto.request;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.dto.request.EnderecoRequest;
import br.com.backend.globals.models.telefone.request.TelefoneRequest;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;

import javax.validation.constraints.*;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    //TODO TESTAR VALIDATORS

    //private Long id; //TODO ID REMOVIDO DO REQUEST. VERIFICAR COMO ISSO PODE IMPACTAR A ATUALIZAÇÃO E REMOÇÃO DO CLIENTE

    @NotEmpty(message = "O nome do cliente não pode estar vazio")
    @Size(max = 70, message = "O nome do cliente deve conter no máximo {max} caracteres")
    private String nome;

    @Email(message = "O padrão do campo e-mail é inválido")
    @Size(max = 70, message = "O e-mail do cliente deve conter no máximo {max} caracteres")
    private String email;

    @NotEmpty(message = "O campo Cpf/Cnpj do cliente não pode estar vazio")
    @Pattern(message = "O padrão do campo Cpf/Cnpj é inválido",
            regexp = "^(\\d{2}\\.?\\d{3}\\.?\\d{3}\\/?\\d{4}-?\\d{2}|\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2})$")
    @Size(message = "O campo Cpf/Cnpj do cliente deve ter no máximo {max} caracteres", max = 18)
    private String cpfCnpj;

    @Size(message = "O campo observações deve conter no máximo {max} caracteres", max = 300)
    private String observacoes;

    @NotNull(message = "O status do cliente não pode ser nulo")
    private StatusClienteEnum statusCliente;

    @Pattern(message = "O padrão do campo data de nascimento está inválido",
            regexp = "^([0-2]\\d|(3)[0-1])(\\/)(((0)\\d)|((1)[0-2]))(\\/)\\d{4}$")
    private String dataNascimento;

    @NotNull(message = "O campo tipo pessoa não pode ser nulo")
    private TipoPessoaEnum tipoPessoa;

    private AcessoSistemaEntity acessoSistema;
    private EnderecoRequest endereco;
    private List<TelefoneRequest> telefones;

}
