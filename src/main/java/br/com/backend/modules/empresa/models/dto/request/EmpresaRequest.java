package br.com.backend.modules.empresa.models.dto.request;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
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
