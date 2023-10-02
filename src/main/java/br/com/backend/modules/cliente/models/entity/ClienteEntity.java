package br.com.backend.modules.cliente.models.entity;

import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.exclusao.entity.ExclusaoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
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
@Table(name = "tb_cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idEmpresaResponsavel;

    @Column(nullable = false, unique = true)
    private String asaasId;

    @Column(nullable = false, updatable = false)
    private String dataCadastro;

    @Column(nullable = false, updatable = false)
    private String horaCadastro;

    @Column(nullable = false)
    private String nome;
    private String email;

    @Column(nullable = false)
    private String cpfCnpj;

    @Column(length = 300)
    private String observacoes;
    @Enumerated(EnumType.STRING)
    private StatusClienteEnum statusCliente;
    private String dataNascimento;
    @Enumerated(EnumType.STRING)
    private TipoPessoaEnum tipoPessoa;

    @OneToOne(targetEntity = AcessoSistemaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private AcessoSistemaEntity acessoSistema;

    @OneToOne(targetEntity = ExclusaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ExclusaoEntity exclusao;

    @OneToOne(targetEntity = EnderecoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private EnderecoEntity endereco;

    @OneToOne(targetEntity = ImagemEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ImagemEntity fotoPerfil;

    @OneToMany(targetEntity = PlanoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PlanoEntity> planos;

    @OneToMany(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TelefoneEntity> telefones;

}
