package br.com.backend.globals.models.acesso.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcessoSistemaRequest {

    @NotEmpty(message = "O campo senha não pode ser vazio")
    @Size(max = 200, message = "O campo senha deve ter no máximo {max} caracteres")
    private String senha;
}
