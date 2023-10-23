package br.com.backend.modules.cliente.models.entity;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.exclusao.entity.ExclusaoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.entity.id.ClienteId;
import br.com.backend.modules.cliente.models.enums.StatusClienteEnum;
import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import br.com.backend.modules.cliente.services.utils.ClienteUtils;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClienteId.class)
@Table(name = "tb_sbs_cliente",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ASAAS_CLI", columnNames = {"cod_asaas_cli"}),
        })
public class ClienteEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do cliente - UUID")
    @Column(name = "cod_cliente_cli", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Id
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("Chave primária do cliente - ID da empresa ao qual o cliente faz parte")
    @JoinColumn(name = "cod_empresa_cli", referencedColumnName = "cod_empresa_emp", nullable = false, updatable = false)
    private EmpresaEntity empresa;

    @Comment("Código de identificação do cliente na integradora ASAAS")
    @Column(name = "cod_asaas_cli", nullable = false, updatable = false)
    private String asaasId;

    @CreatedDate
    @Comment("Data em que o cadastro do cliente foi realizado")
    @Column(name = "dt_datacadastro_cli", nullable = false, updatable = false, length = 10)
    private String dataCriacao;

    @CreatedDate
    @Comment("Hora em que o cadastro do cliente foi realizado")
    @Column(name = "hr_horacadastro_cli", nullable = false, updatable = false, length = 18)
    private String horaCriacao;

    @Comment("Nome do cliente")
    @Column(name = "str_nome_cli", nullable = false, length = 70)
    private String nome;

    @Comment("E-mail do cliente")
    @Column(name = "eml_email_cli", length = 70)
    private String email;

    @Comment("CPF ou CNPJ do cliente")
    @Column(name = "cdp_cpfcnpj_cli", nullable = false, length = 18)
    private String cpfCnpj;

    @Comment("Observações sobre o cliente")
    @Column(name = "str_observacoes_cli", length = 300)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Comment("Status atual do cliente: 0 - Comum, 1 - Em débito, 2 - Vip, 3 - Atenção")
    @Column(name = "enm_status_cli", nullable = false)
    private StatusClienteEnum statusCliente;

    @Comment("Caso a pessoa seja FÍSICA, a data de nascimento poderá ser informada")
    @Column(name = "dt_datanascimento_cli", length = 10)
    private String dataNascimento;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo de pessoa do cliente: 0 - Física, 1 - Jurídica")
    @Column(name = "enm_tipopessoa_cli", nullable = false)
    private TipoPessoaEnum tipoPessoa;

    @Comment("Objeto contendo informações de acesso ao sistema do cliente")
    @OneToOne(targetEntity = AcessoSistemaEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_acesso_cli",
            referencedColumnName = "cod_acesso_acs",
            foreignKey = @ForeignKey(name = "FK_ACESSO_CLIENTE"))
    @ToString.Exclude
    private AcessoSistemaEntity acessoSistema;

    @Comment("Código de exclusão do cliente")
    @OneToOne(targetEntity = ExclusaoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_exclusao_cli",
            referencedColumnName = "cod_exclusao_exc",
            foreignKey = @ForeignKey(name = "FK_EXCLUSAO_CLIENTE"))
    @ToString.Exclude
    private ExclusaoEntity exclusao;

    @Comment("Código do endereço do cliente")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_endereco_cli",
            referencedColumnName = "cod_endereco_end",
            foreignKey = @ForeignKey(name = "FK_ENDERECO_CLIENTE"))
    @ToString.Exclude
    private EnderecoEntity endereco;

    @Comment("Código da imagem de perfil do cliente")
    @OneToOne(targetEntity = ImagemEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_imagem_cli",
            referencedColumnName = "cod_imagem_img",
            foreignKey = @ForeignKey(name = "FK_IMAGEM_CLIENTE"))
    @ToString.Exclude
    private ImagemEntity fotoPerfil;

    @Builder.Default
    @ToString.Exclude
    @Comment("Planos de assinatura vinculados ao cliente")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = PlanoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_sbs_cliente_planos",
            uniqueConstraints = {
                    @UniqueConstraint(name = "UK_PLANO_CLIENTE", columnNames = {"cod_plano_pln"}),
                    @UniqueConstraint(name = "UK_PLANO_CLIENTE", columnNames = {"cod_empresa_pln"})
            },
            joinColumns = {
                    @JoinColumn(name = "cod_cliente_cli",
                            referencedColumnName = "cod_cliente_cli",
                            foreignKey = @ForeignKey(name = "FK_CLIENTE_PLANO")),
                    @JoinColumn(name = "cod_empresa_cli",
                            referencedColumnName = "cod_empresa_cli",
                            foreignKey = @ForeignKey(name = "FK_CLIENTE_PLANO")),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "cod_plano_pln",
                            referencedColumnName = "cod_plano_pln",
                            foreignKey = @ForeignKey(name = "FK_PLANO_CLIENTE")),
                    @JoinColumn(name = "cod_empresa_pln",
                            referencedColumnName = "cod_empresa_pln",
                            foreignKey = @ForeignKey(name = "FK_PLANO_CLIENTE"))
            }
    )
    private List<PlanoEntity> planos = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @Comment("Telefones vinculados ao cadastro do cliente")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_sbs_cliente_telefones",
            uniqueConstraints = @UniqueConstraint(name = "UK_TELEFONE_CLIENTE", columnNames = {"cod_telefone_tel"}),
            joinColumns = {
                    @JoinColumn(name = "cod_cliente_cli",
                            referencedColumnName = "cod_cliente_cli",
                            foreignKey = @ForeignKey(name = "FK_CLIENTE_TELEFONE")),
                    @JoinColumn(name = "cod_empresa_cli",
                            referencedColumnName = "cod_empresa_cli",
                            foreignKey = @ForeignKey(name = "FK_CLIENTE_TELEFONE")),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "cod_telefone_tel",
                            referencedColumnName = "cod_telefone_tel")
            }
    )
    private List<TelefoneEntity> telefones = new ArrayList<>();

    public ClienteEntity constroiClienteEntityParaCriacao(EmpresaEntity empresaSessao,
                                                          String asaasId,
                                                          ClienteRequest clienteRequest) {
        return ClienteEntity.builder()
                .empresa(empresaSessao)
                .asaasId(asaasId)
                .dataCriacao(LocalDate.now().toString())
                .horaCriacao(LocalTime.now().toString())
                .nome(clienteRequest.getNome().toUpperCase())
                .email(clienteRequest.getEmail() != null
                        ? clienteRequest.getEmail().toLowerCase()
                        : null)
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .observacoes(clienteRequest.getObservacoes())
                .statusCliente(clienteRequest.getStatusCliente())
                .dataNascimento(clienteRequest.getDataNascimento())
                .tipoPessoa(clienteRequest.getTipoPessoa())
                .acessoSistema(clienteRequest.getAcessoSistema() != null
                        ? AcessoSistemaEntity.builder()
                        .senha(new BCryptPasswordEncoder().encode(clienteRequest.getAcessoSistema().getSenha()))
                        .perfis(new HashSet<>(List.of(PerfilEnum.CLIENTE)))
                        .build()
                        : null)
                .exclusao(null)
                .endereco(clienteRequest.getEndereco() == null
                        ? null
                        : new EnderecoEntity().constroiEnderecoEntity(clienteRequest.getEndereco()))
                .fotoPerfil(null)
                .telefones(clienteRequest.getTelefones() == null
                        ? new ArrayList<>()
                        : new TelefoneEntity().constroiListaTelefoneEntity(clienteRequest.getTelefones()))
                .planos(new ArrayList<>())
                .build();
    }

    public ClienteEntity atualizaEntidadeComAtributosRequest(ClienteEntity clienteEncontrado,
                                                             ClienteRequest clienteAtualizado) {
        return ClienteEntity.builder()
                .empresa(clienteEncontrado.getEmpresa())
                .uuid(clienteEncontrado.getUuid())
                .asaasId(clienteEncontrado.getAsaasId())
                .dataCriacao(clienteEncontrado.getDataCriacao())
                .horaCriacao(clienteEncontrado.getHoraCriacao())
                .nome(clienteAtualizado.getNome().toUpperCase())
                .email(clienteAtualizado.getEmail() != null
                        ? clienteAtualizado.getEmail().toLowerCase()
                        : null)
                .cpfCnpj(clienteAtualizado.getCpfCnpj())
                .observacoes(clienteAtualizado.getObservacoes())
                .statusCliente(clienteAtualizado.getStatusCliente())
                .dataNascimento(clienteAtualizado.getDataNascimento())
                .tipoPessoa(clienteAtualizado.getTipoPessoa())
                .acessoSistema(clienteAtualizado.getAcessoSistema() != null
                        ? AcessoSistemaEntity.builder()
                        .senha(ClienteUtils.realizaTratamentoDeSenhaAtualizacao(this, clienteAtualizado))
                        .perfis(new HashSet<>(List.of(PerfilEnum.CLIENTE)))
                        .build()
                        : null)
                .exclusao(clienteEncontrado.getExclusao())
                .endereco(clienteAtualizado.getEndereco() == null
                        ? null
                        : new EnderecoEntity().constroiEnderecoEntity(clienteAtualizado.getEndereco()))
                .fotoPerfil(clienteEncontrado.getFotoPerfil())
                .planos(clienteEncontrado.getPlanos())
                .telefones(clienteAtualizado.getTelefones() == null
                        ? new ArrayList<>()
                        : new TelefoneEntity().constroiListaTelefoneEntity(clienteAtualizado.getTelefones()))
                .build();

    }

    public void criaExclusao() {
        this.setExclusao(new ExclusaoEntity().constroiObjetoExclusao());
    }

    public void addPlano(PlanoEntity planoEntity) {
        this.planos.add(planoEntity);
    }

    public PlanoEntity obtemUltimoPlanoPersistido() {
        int indicePlano = this.getPlanos().size() - 1;
        return this.getPlanos().get(indicePlano);
    }
}
