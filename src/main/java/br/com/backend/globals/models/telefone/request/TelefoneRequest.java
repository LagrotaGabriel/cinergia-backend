package br.com.backend.globals.models.telefone.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneRequest {

    @NotEmpty(message = "O prefixo do telefone não pode estar vazio")
    @Min(value = 10, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    @Max(value = 99, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    @Pattern(regexp = "\\d{2}", message = "O padrão do prefixo do telefone é inválido")
    private Integer prefixo;

    @NotEmpty(message = "O número do telefone não pode estar vazio")
    @Min(value = 8, message = "O número do telefone deve conter no mínimo {min} caracteres numéricos")
    @Min(value = 9, message = "O número do telefone deve conter no máximo {max} caracteres numéricos")
    @Pattern(regexp = "(\\d{9})|(\\d{8})", message = "O padrão do número do telefone é inválido")
    private Integer numero;

    public String obtemPrefixoComNumeroJuntos() {
        return this.getPrefixo().toString() + this.getNumero().toString();
    }
}
