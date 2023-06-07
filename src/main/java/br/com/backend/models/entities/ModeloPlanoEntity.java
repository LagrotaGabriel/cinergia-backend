package br.com.backend.models.entities;

import br.com.backend.models.enums.CicloCobrancaEnum;
import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_modelo_plano")
public class ModeloPlanoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricaoPlano;
    private Double valor;
    @Enumerated(EnumType.STRING)
    private CicloCobrancaEnum cicloCobranca;

}
