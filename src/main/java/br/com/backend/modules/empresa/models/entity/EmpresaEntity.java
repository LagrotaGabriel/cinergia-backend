package br.com.backend.modules.empresa.models.entity;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_empresa",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_EMAIL_EMPRESA", columnNames = {"eml_email_emp"}),
                @UniqueConstraint(name = "UK_CPFCNPJ_EMPRESA", columnNames = {"cdp_cpfcnpj_emp"}),
                @UniqueConstraint(name = "UK_TELEFONE_EMPRESA", columnNames = {"cod_telefone_emp"}),
                @UniqueConstraint(name = "UK_ENDERECO_EMPRESA", columnNames = {"cod_endereco_emp"}),
        })
public class EmpresaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da empresa - UUID")
    @Column(name = "cod_empresa_emp", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Data em que o cadastro da empresa foi realizado")
    @Column(name = "dt_datacadastro_emp", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da empresa foi realizado")
    @Column(name = "hr_horacadastro_emp", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Nome da empresa")
    @Column(name = "str_nome_emp", nullable = false, length = 70)
    private String nomeEmpresa;

    @Comment("E-mail da empresa")
    @Column(name = "eml_email_emp", nullable = false, length = 70)
    private String email;

    @Comment("CPF ou CNPJ da empresa. Caso a empresa não possua um CNPJ, é possível se cadastrar utilizando o CPF")
    @Column(name = "cdp_cpfcnpj_emp", nullable = false, length = 18)
    private String cpfCnpj;

    @Comment("Caso a pessoa seja FÍSICA, a data de nascimento deverá ser informada")
    @Column(name = "dt_datanascimento_emp", length = 10)
    private String dataNascimento;

    @Comment("Caso a pessoa seja JURÍDICA, a inscrição estadual deverá ser informada")
    @Column(name = "ine_inscricaoestadual_emp", length = 12)
    private String inscricaoEstadual;

    @Comment("Saldo atual da empresa")
    @Column(name = "mon_saldo_emp", nullable = false, scale = 2)
    private Double saldo;

    @Comment("Código do endereço da empresa")
    @OneToOne(targetEntity = EnderecoEntity.class,
            orphanRemoval = true,
            optional = false,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_endereco_emp",
            referencedColumnName = "cod_endereco_end",
            foreignKey = @ForeignKey(name = "FK_ENDERECO_EMPRESA"))
    @ToString.Exclude
    private EnderecoEntity endereco;

    @Comment("Código do telefone da empresa")
    @OneToOne(targetEntity = TelefoneEntity.class,
            orphanRemoval = true,
            optional = false,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_telefone_emp",
            referencedColumnName = "cod_telefone_tel",
            foreignKey = @ForeignKey(name = "FK_TELEFONE_EMPRESA"))
    @ToString.Exclude
    private TelefoneEntity telefone;

    @Comment("Objeto contendo informações de acesso ao sistema da empresa")
    @OneToOne(targetEntity = AcessoSistemaEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "cod_acesso_emp",
            referencedColumnName = "cod_acesso_acs",
            foreignKey = @ForeignKey(name = "FK_ACESSO_EMPRESA"))
    private AcessoSistemaEntity acessoSistema;

    @Comment("Código da imagem de perfil da empresa")
    @OneToOne(targetEntity = ImagemEntity.class,
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_imagem_emp",
            referencedColumnName = "cod_imagem_img",
            foreignKey = @ForeignKey(name = "FK_IMAGEM_EMPRESA"))
    @ToString.Exclude
    private ImagemEntity logoEmpresa;

    public EmpresaEntity constroiEmpresaEntityParaCriacao(EmpresaRequest empresaRequest) {
        return EmpresaEntity.builder()
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .nomeEmpresa(empresaRequest.getNomeEmpresa().toUpperCase())
                .email(empresaRequest.getEmail() != null
                        ? empresaRequest.getEmail().toLowerCase()
                        : null)
                .cpfCnpj(empresaRequest.getCpfCnpj())
                .dataNascimento(empresaRequest.getDataNascimento())
                .inscricaoEstadual(empresaRequest.getInscricaoEstadual())
                .saldo(0.0)
                .endereco(empresaRequest.getEndereco() == null
                        ? null
                        : new EnderecoEntity().constroiEnderecoEntity(empresaRequest.getEndereco()))
                .telefone(empresaRequest.getTelefone() == null
                        ? null
                        : new TelefoneEntity().constroiTelefoneEntity(empresaRequest.getTelefone()))
                .acessoSistema(empresaRequest.getAcessoSistema() != null
                        ? AcessoSistemaEntity.builder()
                        .senha(new BCryptPasswordEncoder().encode(empresaRequest.getAcessoSistema().getSenha()))
                        .perfis(new HashSet<>(List.of(PerfilEnum.EMPRESA)))
                        .build()
                        : null)
                .logoEmpresa(null)
                .build();
    }

}
