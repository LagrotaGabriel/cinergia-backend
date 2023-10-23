package br.com.backend.globals.models.acesso.entity;

import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_acesso")
public class AcessoSistemaEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do acesso - UUID")
    @Column(name = "cod_acesso_acs", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Senha do acesso")
    @Column(name = "str_senha_acs", nullable = false, length = 200)
    private String senha;

    @ToString.Exclude
    @Builder.Default
    @Comment("Perfis de acesso")
    @CollectionTable(name = "tb_sbs_acesso_perfis")
    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<PerfilEnum> perfis = new HashSet<>();
}