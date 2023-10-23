package br.com.backend.globals.models.endereco.entity;

import br.com.backend.globals.models.endereco.enums.EstadoEnum;
import br.com.backend.globals.models.endereco.dto.request.EnderecoRequest;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_endereco")
public class EnderecoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do endereço - UUID")
    @Column(name = "cod_endereco_end", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Logradouro do endereço")
    @Column(name = "str_logradouro_end", nullable = false, length = 100)
    private String logradouro;

    @Comment("Número do endereço")
    @Column(name = "int_numero_end", nullable = false)
    private Integer numero;

    @Comment("Bairro do endereço")
    @Column(name = "str_bairro_end", length = 80)
    private String bairro;

    @Comment("Cep do endereço")
    @Column(name = "str_codigopostal_end", length = 8)
    private String codigoPostal;

    @Comment("Cidade do endereço")
    @Column(name = "str_cidade_end", length = 80)
    private String cidade;

    @Comment("Complemento do endereço")
    @Column(name = "str_complemento_end", length = 100)
    private String complemento;

    @Enumerated(EnumType.STRING)
    @Comment("Estado do endereço")
    @Column(name = "enm_estado_end", nullable = false)
    private EstadoEnum estado;

    public EnderecoEntity constroiEnderecoEntity(EnderecoRequest enderecoRequest) {
        return EnderecoEntity.builder()
                .logradouro(enderecoRequest.getLogradouro())
                .numero(enderecoRequest.getNumero())
                .bairro(enderecoRequest.getBairro())
                .codigoPostal(enderecoRequest.getCodigoPostal())
                .cidade(enderecoRequest.getCidade())
                .complemento(enderecoRequest.getComplemento())
                .estado(enderecoRequest.getEstado())
                .build();
    }

}
