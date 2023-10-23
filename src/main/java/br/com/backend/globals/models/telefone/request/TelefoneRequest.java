package br.com.backend.globals.models.telefone.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneRequest {

    @NotNull(message = "O prefixo do telefone não pode ser nulo")
    @Min(value = 10, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    @Max(value = 99, message = "O prefixo do telefone deve conter 2 caracteres numéricos")
    private Integer prefixo;

    @NotNull(message = "O número do telefone não pode ser nulo")
    @Min(value = 8, message = "O número do telefone deve conter no mínimo {value} caracteres numéricos")
    @Min(value = 9, message = "O número do telefone deve conter no máximo {value} caracteres numéricos")
    private Integer numero;

    public String obtemPrefixoComNumeroJuntos() {
        return this.getPrefixo().toString() + this.getNumero().toString();
    }
}
