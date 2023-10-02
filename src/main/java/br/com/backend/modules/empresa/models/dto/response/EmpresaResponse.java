package br.com.backend.modules.empresa.models.dto.response;

import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
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

    private String dataNascimento;
    private Double saldo;

    private EnderecoEntity endereco;

    private TelefoneEntity telefone;

}