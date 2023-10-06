package br.com.backend.globals.models.telefone.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneRequest {

    @NotEmpty(message = "O prefixo do telefone não pode estar vazio")
    @Pattern(regexp = "(\\d{2})", message = "O padrão do prefixo do telefone é inválido")
    private String prefixo;

    @NotEmpty(message = "O número do telefone não pode estar vazio")
    @Pattern(regexp = "(\\d{5}-\\d{4})|(\\d{4}-\\d{4})", message = "O padrão do número do telefone é inválido")
    private String numero;

    public String obtemPrefixoComNumeroJuntos() {
        return this.getPrefixo() + this.getNumero();
    }
}
