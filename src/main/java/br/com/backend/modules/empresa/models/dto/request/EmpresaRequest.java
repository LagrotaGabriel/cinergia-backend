package br.com.backend.modules.empresa.models.dto.request;

import br.com.backend.globals.models.acesso.request.AcessoSistemaRequest;
import br.com.backend.globals.models.endereco.dto.request.EnderecoRequest;
import br.com.backend.globals.models.telefone.request.TelefoneRequest;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    @NotEmpty(message = "O nome da empresa não pode estar vazio")
    @Size(max = 70, message = "O nome da empresa deve conter no máximo {max} caracteres")
    private String nomeEmpresa;

    @Email(message = "O padrão do campo e-mail é inválido")
    @Size(max = 70, message = "O e-mail da empresa deve conter no máximo {max} caracteres")
    private String email;

    @NotEmpty(message = "O campo Cpf/Cnpj da empresa não pode estar vazio")
    @Pattern(regexp = "(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})|(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})",
            message = "O padrão do campo Cpf/Cnpj é inválido")
    @Size(message = "O Cpf/Cnpj da empresa deve ter no máximo {max} caracteres", max = 18)
    private String cpfCnpj;

    @Pattern(regexp = "([0-2]\\d|3[0-1])/(0\\d|1[0-2])/((19|20)\\d\\d)",
            message = "O padrão do campo data de nascimento está inválido")
    private String dataNascimento;

    @Pattern(regexp = "\\d{12}", message = "O padrão da inscrição estadual está inválido")
    private String inscricaoEstadual;

    @Valid
    @NotNull(message = "O endereço não pode estar vazio")
    private EnderecoRequest endereco;

    @Valid
    @NotNull(message = "O telefone não pode estar vazio")
    private TelefoneRequest telefone;

    @Valid
    @NotNull(message = "O acesso não pode estar vazio")
    private AcessoSistemaRequest acessoSistema;
}
