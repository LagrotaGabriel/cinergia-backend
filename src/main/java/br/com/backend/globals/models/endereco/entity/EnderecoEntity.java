package br.com.backend.globals.models.endereco.entity;

import br.com.backend.globals.models.endereco.enums.EstadoEnum;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SBS_ENDERECO")
public class EnderecoEntity {

    @Id
    @Comment("Chave primária dO endereço - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(table = "TB_SBS_ENDERECO", name = "COD_ENDERECO_END", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Comment("Logradouro do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "STR_LOGRADOURO_END", nullable = false, length = 100)
    private String logradouro;

    @Comment("Número do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "INT_NUMERO_END", nullable = false)
    private Integer numero;

    @Comment("Bairro do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "STR_BAIRRO_END", length = 70)
    private String bairro;

    @Comment("Cep do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "STR_CODIGOPOSTAL_END", length = 8)
    private String codigoPostal;

    @Comment("Cidade do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "STR_CODIGOPOSTAL_END", length = 70)
    private String cidade;

    @Comment("Complemento do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "STR_COMPLEMENTO_END", length = 100)
    private String complemento;

    @Enumerated(EnumType.STRING)
    @Comment("Estado do endereço")
    @Column(table = "TB_SBS_ENDERECO", name = "ENM_ESTADO_END", nullable = false)
    private EstadoEnum estado;

}
