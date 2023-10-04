package br.com.backend.globals.models.acesso.entity;

import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SBS_ACESSO")
public class AcessoSistemaEntity {

    @Id
    @Comment("Chave primária do acesso - UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(table = "TB_SBS_ACESSO", name = "COD_ACESSO_ACS", nullable = false, updatable = false, length = 36)
    private UUID uuid;

    @Comment("Senha do acesso")
    @Column(table = "TB_SBS_ACESSO", name = "STR_SENHA_ACS", nullable = false, length = 200)
    private String senha;

    @Comment("Perfis de acesso")
    @Column(table = "TB_SBS_ACESSO", name = "LST_PERFIS_ACS", nullable = false)
    @CollectionTable(name = "PERFIS")
    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<PerfilEnum> perfis = new HashSet<>();
}