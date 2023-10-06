package br.com.backend.globals.models.telefone.entity;

import br.com.backend.globals.models.telefone.request.TelefoneRequest;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_sbs_telefone")
public class TelefoneEntity {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @Comment("Chave primária do telefone - UUID")
    @Column(name = "cod_telefone_tel", nullable = false, updatable = false)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Comment("Prefixo do telefone")
    @Column(name = "str_prefixo_tel", nullable = false, length = 2)
    private String prefixo;

    @Comment("Número do telefone")
    @Column(name = "str_numero_tel", nullable = false, length = 9)
    private String numero;

    public TelefoneEntity constroiTelefoneEntity(TelefoneRequest telefoneRequest) {
        return TelefoneEntity.builder()
                .prefixo(telefoneRequest.getPrefixo())
                .numero(telefoneRequest.getNumero())
                .build();
    }

    public List<TelefoneEntity> constroiListaTelefoneEntity(List<TelefoneRequest> telefonesRequest) {
        List<TelefoneEntity> telefonesEntity = new ArrayList<>();

        telefonesRequest.forEach(telefoneRequest ->
                telefonesEntity.add(constroiTelefoneEntity(telefoneRequest)));

        return telefonesEntity;
    }
}
