package br.com.backend.models.entities;

import br.com.backend.models.enums.TipoChavePixEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_transferencia")
public class TransferenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idEmpresaResponsavel;

    @Column(nullable = false)
    private String dataCadastro;

    @Column(nullable = false)
    private String horaCadastro;

    private String descricao;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String chavePix;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoChavePixEnum tipoChavePix;
}
