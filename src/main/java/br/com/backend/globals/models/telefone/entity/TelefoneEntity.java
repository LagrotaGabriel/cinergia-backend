package br.com.backend.globals.models.telefone.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SBS_TELEFONE")
public class TelefoneEntity {

    @Id
    @Comment("Chave primária do telefone - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TELEFONE_TEL", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Comment("Prefixo do telefone")
    @Column(name = "STR_PREFIXO_TEL", nullable = false, length = 2)
    private String prefixo;

    @Comment("Número do telefone")
    @Column(name = "STR_NUMERO_TEL", nullable = false, length = 9)
    private String numero;
}
