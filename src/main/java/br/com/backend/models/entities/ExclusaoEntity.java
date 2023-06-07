package br.com.backend.models.entities;

import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_exclusao")
public class ExclusaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dataExclusao;
    private String horaExclusao;
}
