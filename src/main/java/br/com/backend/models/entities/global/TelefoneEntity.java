package br.com.backend.models.entities.global;

import br.com.backend.models.enums.global.TipoTelefoneEnum;
import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_telefone")
public class TelefoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prefixo;

    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTelefoneEnum tipoTelefone;

}
