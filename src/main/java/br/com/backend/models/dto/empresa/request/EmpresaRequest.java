package br.com.backend.models.dto.empresa.request;

import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import br.com.backend.models.enums.TipoPessoaEnum;
import lombok.*;

import javax.persistence.Column;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {
    private Long id;

    @Column(nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    @Column(nullable = false)
    private TipoPessoaEnum tipoPessoaEnum;

    private EnderecoEntity endereco;

    private TelefoneEntity telefone;

    @Column(nullable = false)
    private AcessoSistemaEntity acessoSistema;
}
