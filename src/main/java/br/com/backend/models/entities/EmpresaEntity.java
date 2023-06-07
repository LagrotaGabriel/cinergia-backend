package br.com.backend.models.entities;

import br.com.backend.models.entities.global.ArquivoEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import br.com.backend.models.enums.TipoPessoaEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_empresa")
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dataCadastro;

    @Column(nullable = false)
    private String horaCadastro;

    @Column(nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false)
    private String email;

    @Column(unique = true)
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    private TipoPessoaEnum tipoPessoaEnum;

    @OneToOne(targetEntity = EnderecoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private EnderecoEntity endereco;

    @OneToOne(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private TelefoneEntity telefone;

    @OneToOne(targetEntity = AcessoSistemaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private AcessoSistemaEntity acessoSistema;

    @OneToOne(targetEntity = ArquivoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ArquivoEntity logoEmpresa;

    @OneToMany(targetEntity = ModeloPlanoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ModeloPlanoEntity> modelosPlano;

    @OneToMany(targetEntity = ClienteEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ClienteEntity> clientes;

}
