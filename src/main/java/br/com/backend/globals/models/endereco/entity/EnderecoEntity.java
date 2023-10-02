package br.com.backend.globals.models.endereco.entity;

import br.com.backend.globals.models.endereco.enums.EstadoEnum;

import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false)
    private Integer numero;
    private String bairro;
    private String codigoPostal;
    private String cidade;
    private String complemento;

    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

}
