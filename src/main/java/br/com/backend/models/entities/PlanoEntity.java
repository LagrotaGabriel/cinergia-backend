package br.com.backend.models.entities;

import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import javax.persistence.*;
import lombok.*;

import java.util.List;

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
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private Double valor;
    private String dataVencimento;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPlanoEnum statusPlano;

    @OneToMany(targetEntity = PagamentoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PagamentoEntity> pagamentos;

}
