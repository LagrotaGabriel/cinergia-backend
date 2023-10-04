package br.com.backend.globals.models.exclusao.entity;

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
@Table(name = "TB_SBS_EXCLUSAO")
public class ExclusaoEntity {

    @Id
    @Comment("Chave primária da exclusão - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(table = "TB_SBS_EXCLUSAO", name = "COD_EXCLUSAO_EXC", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Comment("Data em que a exclusão foi realizada")
    @Column(table = "TB_SBS_EXCLUSAO", name = "DT_DATACADASTRO_EXC", nullable = false, updatable = false, length = 10)
    private String dataExclusao;

    @Comment("Hora em que a exclusão foi realizado")
    @Column(table = "TB_SBS_EXCLUSAO", name = "HR_HORACADASTRO_EXC", nullable = false, updatable = false, length = 18)
    private String horaExclusao;
}
