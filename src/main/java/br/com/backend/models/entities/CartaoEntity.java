package br.com.backend.models.entities;

import br.com.backend.models.enums.BandeiraCartaoEnum;
import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartao")
public class CartaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dataCadastro;
    private String horaCadastro;
    private Boolean ativo;
    private String nomePortador;
    private String cpfCnpjPortador;
    private Long numero;
    private Integer ccv;
    private Integer mesExpiracao;
    private Integer anoExpiracao;
    private String tokenCartao;

    @Enumerated(EnumType.STRING)
    private BandeiraCartaoEnum bandeiraCartaoEnum;

    @OneToOne(targetEntity = ExclusaoEntity.class, orphanRemoval = true, cascade = CascadeType.ALL)
    private ExclusaoEntity exclusao;

}
