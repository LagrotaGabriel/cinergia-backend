package br.com.backend.globals.models.exclusao.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_exclusao")
public class ExclusaoEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária da exclusão - UUID")
    @Column(name = "cod_exclusao_exc", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Data em que a exclusão foi realizada")
    @Column(name = "dt_datacadastro_exc", nullable = false, updatable = false, length = 10)
    private String dataExclusao;

    @Comment("Hora em que a exclusão foi realizado")
    @Column(name = "hr_horacadastro_exc", nullable = false, updatable = false, length = 18)
    private String horaExclusao;

    public ExclusaoEntity constroiObjetoExclusao() {
        return ExclusaoEntity.builder()
                .dataExclusao(LocalDate.now().toString())
                .horaExclusao(LocalTime.now().toString())
                .build();
    }
}
