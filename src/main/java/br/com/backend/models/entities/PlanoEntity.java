package br.com.backend.models.entities;

import br.com.backend.models.enums.*;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_plano")
public class PlanoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long idEmpresaResponsavel;
    @Column(nullable = false)
    private Long idClienteResponsavel;
    @Column(nullable = false)
    private String dataCadastro;
    @Column(nullable = false)
    private String horaCadastro;
    private String dataInicio;
    private String descricao;
    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPlanoEnum statusPlano;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PeriodicidadeEnum periodicidade;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "NOTIFICACOES")
    protected Set<NotificacaoEnum> notificacoes = new HashSet<>();

    @OneToOne(targetEntity = CartaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private CartaoEntity cartao;

    @OneToMany(targetEntity = PagamentoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PagamentoEntity> pagamentos;

}
