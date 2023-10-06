package br.com.backend.globals.models.endereco.dto.request;

import br.com.backend.globals.models.endereco.enums.EstadoEnum;
import lombok.*;

import javax.validation.constraints.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {
    @NotEmpty(message = "O logradouro não pode estar vazio")
    @Size(max = 100, message = "O logradouro deve conter no máximo {max} caracteres")
    private String logradouro;

    @NotNull(message = "O campo número não pode ser nulo")
    @Min(value = 1, message = "O valor mínimo para o campo número é {min}")
    @Max(value = 99999, message = "O valor máximo para o campo número é {max}")
    private Integer numero;

    @Size(max = 80, message = "O logradouro deve conter no máximo {max} caracteres")
    private String bairro;

    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 caracteres numéricos")
    private String codigoPostal;

    @Size(max = 80, message = "A cidade deve conter no máximo {max} caracteres")
    private String cidade;

    @Size(max = 100, message = "O complemento deve conter no máximo {max} caracteres")
    private String complemento;

    @NotNull(message = "O campo estado não pode ser nulo")
    private EstadoEnum estado;
}
