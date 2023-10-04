package br.com.backend.globals.models.imagem.entity;

import br.com.backend.globals.models.imagem.enums.TipoImagemEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SBS_IMAGEM")
public class ImagemEntity {

    @Id
    @Comment("Chave primária da imagem - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_IMAGEM_IMG", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Comment("Nome do arquivo")
    @Column(name = "STR_NOME_IMG", nullable = false, length = 70)
    private String nome;

    @Comment("Tamanho da imagem")
    @Column(name = "LNG_TAMANHO_IMG", nullable = false)
    private Long tamanho;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo do arquivo da imagem: 0 - Jpeg, 1 - Png")
    @Column(name = "ENM_TIPO_IMG", nullable = false)
    private TipoImagemEnum tipo;

    @Lob
    @Comment("Arquivo de imagem")
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "ARQ_ARQUIVO_IMG", nullable = false)
    private byte[] arquivo;

}
