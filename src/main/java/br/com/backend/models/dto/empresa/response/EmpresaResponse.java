package br.com.backend.models.dto.empresa.response;

import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import br.com.backend.models.enums.TipoPessoaEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private Long id;
    private String dataCadastro;
    private String horaCadastro;
    private String nomeEmpresa;

    private String email;

    private String cpfCnpj;

    private TipoPessoaEnum tipoPessoaEnum;

    private EnderecoEntity endereco;

    private TelefoneEntity telefone;

}