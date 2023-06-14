package br.com.backend.models.entities.global;

import br.com.backend.models.enums.global.TipoImagemEnum;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_imagem")
public class ImagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Long tamanho;
    @Enumerated(EnumType.STRING)
    private TipoImagemEnum tipo;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] arquivo;

}
