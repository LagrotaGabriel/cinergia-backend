package br.com.backend.modules.plano.models.entity;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import lombok.*;

import javax.persistence.*;
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
    @Column(unique = true)
    private String idAsaas;
    @Column(nullable = false)
    private String dataCadastro;
    @Column(nullable = false)
    private String horaCadastro;
    private String dataVencimento;
    @Column(nullable = false)
    private String dataInicio;
    @Column(nullable = false)
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

    @OneToMany(targetEntity = PagamentoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PagamentoEntity> pagamentos;

}
