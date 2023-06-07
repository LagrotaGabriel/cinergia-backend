package br.com.backend.models.entities.global;

import br.com.backend.models.enums.global.TipoArquivoEnum;
import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_arquivo")
public class ArquivoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Long tamanho;
    @Enumerated(EnumType.STRING)
    private TipoArquivoEnum tipo;
    @Lob
    private byte[] arquivo;

}
