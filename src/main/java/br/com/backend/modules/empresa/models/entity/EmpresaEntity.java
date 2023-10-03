package br.com.backend.modules.empresa.models.entity;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.imagem.entity.ImagemEntity;
import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
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
@Table(name = "TB_SBS_EMPRESA")
public class EmpresaEntity {

    @Id
    @Comment("Chave primária da empresa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(table = "TB_SBS_EMPRESA", name = "COD_EMPRESA", nullable = false, unique = true, updatable = false, length = 36)
    private UUID id;

    @Comment("Data em que o cadastro da empresa foi realizado")
    @Column(table = "TB_SBS_EMPRESA", name = "DAT_DATACADASTRO_EMP", nullable = false, updatable = false, length = 10)
    private String dataCadastro;

    @Comment("Hora em que o cadastro da empresa foi realizado")
    @Column(table = "TB_SBS_EMPRESA", name = "HOR_HORACADASTRO_EMP", nullable = false, updatable = false, length = 18)
    private String horaCadastro;

    @Comment("Nome da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "STR_NOME_EMP", nullable = false, length = 70)
    private String nomeEmpresa;

    @Comment("E-mail da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "EML_EMAIL_EMP", nullable = false, unique = true, length = 70)
    private String email;

    @Comment("CPF ou CNPJ da empresa. Caso a empresa não possua um CNPJ, é possível se cadastrar utilizando o CPF")
    @Column(table = "TB_SBS_EMPRESA", name = "CDP_CPFCNPJ_EMP", nullable = false, unique = true, length = 18)
    private String cpfCnpj;

    @Comment("Caso a pessoa seja FÍSICA, a data de nascimento deverá ser informada")
    @Column(table = "TB_SBS_EMPRESA", name = "DAT_DATANASCIMENTO_EMP", length = 10)
    private String dataNascimento;

    @Comment("Caso a pessoa seja JURÍDICA, a inscrição estadual deverá ser informada")
    @Column(table = "TB_SBS_EMPRESA", name = "INE_INSCRICAOESTADUAL_EMP", length = 15)
    private String inscricaoEstadual;

    @Comment("Saldo atual da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "MON_SALDO_EMP", nullable = false, scale = 2)
    private Double saldo;

    @Comment("Endereço da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "COD_ENDERECO_EMP", nullable = false)
    @OneToOne(targetEntity = EnderecoEntity.class, orphanRemoval = true, optional = false, cascade = CascadeType.ALL)
    private EnderecoEntity endereco;

    @Comment("Telefone da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "COD_TELEFONE_EMP", nullable = false)
    @OneToOne(targetEntity = TelefoneEntity.class, orphanRemoval = true, optional = false, cascade = CascadeType.ALL)
    private TelefoneEntity telefone;

    @Comment("Objeto contendo informações de acesso ao sistema da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "COD_ACESSO_EMP", nullable = false)
    @OneToOne(targetEntity = AcessoSistemaEntity.class, orphanRemoval = true, optional = false, cascade = CascadeType.ALL)
    private AcessoSistemaEntity acessoSistema;

    @Comment("Logotipo da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "COD_LOGO_EMP")
    @OneToOne(targetEntity = ImagemEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ImagemEntity logoEmpresa;

    @ToString.Exclude
    @Comment("Lista de clientes cadastrador pela empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "LST_CLIENTES_EMP", nullable = false)
    @OneToMany(targetEntity = ClienteEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ClienteEntity> clientes = new ArrayList<>();

    @ToString.Exclude
    @Comment("Lista de transferências realizadas pela empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "LST_TRANSFERENCIAS_EMP", nullable = false)
    @OneToMany(targetEntity = TransferenciaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TransferenciaEntity> transferencias = new ArrayList<>();

    @Comment("Lista de notificações da empresa")
    @Column(table = "TB_SBS_EMPRESA", name = "LST_NOTIFICACOES_EMP", nullable = false)
    @OneToMany(targetEntity = NotificacaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<NotificacaoEntity> notificacoes = new ArrayList<>();

}
