package br.com.backend.globals.models.imagem.entity;

import br.com.backend.globals.models.imagem.enums.TipoImagemEnum;
import br.com.backend.globals.models.imagem.utils.ImagemUtils;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_imagem")
public class ImagemEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da imagem - UUID")
    @Column(name = "cod_imagem_img", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Nome do arquivo")
    @Column(name = "str_nome_img", nullable = false, length = 70)
    private String nome;

    @Comment("Tamanho da imagem")
    @Column(name = "lng_tamanho_img", nullable = false)
    private Long tamanho;

    @Enumerated(EnumType.STRING)
    @Comment("Tipo do arquivo da imagem: 0 - Jpeg, 1 - Png")
    @Column(name = "enm_tipo_img", nullable = false)
    private TipoImagemEnum tipo;

    @Lob
    @Comment("Arquivo de imagem")
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "ARQ_ARQUIVO_IMG", nullable = false)
    private byte[] arquivo;

    public ImagemEntity constroiImagemEntity(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) return null;
        return ImagemEntity.builder()
                .nome(multipartFile.getOriginalFilename())
                .tipo(ImagemUtils.realizaTratamentoTipoDeImagem(Objects.requireNonNull(multipartFile.getContentType())))
                .tamanho(multipartFile.getSize())
                .arquivo(multipartFile.getBytes())
                .build();
    }

}
