package br.com.backend.modules.transferencia.models.entity.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaId implements Serializable {

    @Type(type = "uuid-char")
    private UUID empresa;
    private UUID uuid;
}
