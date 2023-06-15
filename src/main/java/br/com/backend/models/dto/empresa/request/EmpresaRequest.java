package br.com.backend.models.dto.empresa.request;

import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {
    private Long id;

    @NotNull
    private String nomeEmpresa;

    @NotNull
    private String email;

    @NotNull
    private String cpfCnpj;

    @NotNull
    private String dataNascimento;

    @NotNull
    private EnderecoEntity endereco;

    @NotNull
    private TelefoneEntity telefone;

    @NotNull
    private TelefoneEntity celular;

    @NotNull
    private AcessoSistemaEntity acessoSistema;
}
