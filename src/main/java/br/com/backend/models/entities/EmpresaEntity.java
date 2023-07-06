package br.com.backend.models.entities;

import br.com.backend.models.entities.global.ArquivoEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.entities.global.TelefoneEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private String dataCadastro;

    @JsonIgnore
    @Column(nullable = false)
    private String horaCadastro;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nomeEmpresa;

    @JsonIgnore
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(unique = true)
    private String cpfCnpj;

    @JsonIgnore
    private String dataNascimento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double saldo;

    @JsonIgnore
    @OneToOne(targetEntity = EnderecoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private EnderecoEntity endereco;

    @JsonIgnore
    @OneToOne(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private TelefoneEntity telefone;

    @JsonIgnore
    @OneToOne(targetEntity = TelefoneEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private TelefoneEntity celular;

    @JsonIgnore
    @OneToOne(targetEntity = AcessoSistemaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private AcessoSistemaEntity acessoSistema;

    @OneToOne(targetEntity = ArquivoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ArquivoEntity logoEmpresa;

    @JsonIgnore
    @OneToMany(targetEntity = ModeloPlanoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ModeloPlanoEntity> modelosPlano;

    @JsonIgnore
    @OneToMany(targetEntity = ClienteEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ClienteEntity> clientes;

    @JsonIgnore
    @OneToMany(targetEntity = TransferenciaEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TransferenciaEntity> transferencias = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = NotificacaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<NotificacaoEntity> notificacoes = new ArrayList<>();

}
