package br.com.backend.modules.cliente.models.entity;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.exclusao.entity.ExclusaoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClienteId.class)
@Table(name = "TB_SBS_CLIENTE")
public class ClienteEntity {

    @Id
    @Comment("Chave primária do cliente - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CLIENTE_CLI", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Id
    @ManyToOne
    @EqualsAndHashCode.Include
    @Comment("Chave primária do cliente - ID da empresa ao qual o cliente faz parte")
    @JoinColumn(name = "COD_EMPRESA_CLI", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Código de identificação do cliente na integradora ASAAS")
    @Column(name = "COD_ASAAS_CLI", updatable = false, nullable = false, unique = true)
    private String asaasId;

    @Comment("Data em que o cadastro do cliente foi realizado")
    @Column(name = "DT_DATACADASTRO_CLI", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro do cliente foi realizado")
    @Column(name = "HR_HORACADASTRO_CLI", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Nome do cliente")
    @Column(name = "STR_NOME_CLI", nullable = false, length = 70)
    private String nome;

    @Comment("E-mail do cliente")
    @Column(name = "EML_EMAIL_CLI", length = 70)
    private String email;

    @Comment("CPF ou CNPJ do cliente")
    @Column(name = "CDP_CPFCNPJ_CLI", nullable = false, unique = true, length = 18)
    private String cpfCnpj;

    @Comment("Observações sobre o cliente")
    @Column(name = "STR_OBSERVACOES_CLI", length = 300)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do cliente: 0 - Comum, 1 - Em débito, 2 - Vip, 3 - Atenção")
    @Column(name = "ENM_STATUS_CLI", nullable = false)
    private StatusClienteEnum statusCliente;

    @Comment("Caso a pessoa seja FÍSICA, a data de nascimento poderá ser informada")
    @Column(name = "DT_DATANASCIMENTO_CLI", length = 10)
    private String dataNascimento;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo de pessoa do cliente: 0 - Física, 1 - Jurídica")
    @Column(name = "ENM_TIPOPESSOA_CLI", nullable = false)
    private TipoPessoaEnum tipoPessoa;

    @Comment("Objeto contendo informações de acesso ao sistema do cliente")
    @Column(name = "COD_ACESSO_CLI")
    @OneToOne(targetEntity = AcessoSistemaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private AcessoSistemaEntity acessoSistema;

    @Comment("Código de exclusão do cliente")
    @Column(name = "COD_EXCLUSAO_CLI")
    @OneToOne(targetEntity = ExclusaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ExclusaoEntity exclusao;

    @Comment("Código do endereço do cliente")
    @Column(name = "COD_ENDERECO_CLI")
    @OneToOne(targetEntity = EnderecoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private EnderecoEntity endereco;

    @Comment("Código da imagem de perfil do cliente")
    @Column(name = "COD_FOTOPERFIL_CLI")
    @OneToOne(targetEntity = ImagemEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ImagemEntity fotoPerfil;

    @Builder.Default
    @ToString.Exclude
    @Comment("Planos de assinatura vinculados ao cliente")
    @Column(name = "LST_PLANOS_CLI")
    @OneToMany(targetEntity = PlanoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PlanoEntity> planos = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Telefones vinculados ao cliente")
    @Column(name = "LST_TELEFONES_CLI")
    @OneToMany(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TelefoneEntity> telefones = new ArrayList<>();

}
