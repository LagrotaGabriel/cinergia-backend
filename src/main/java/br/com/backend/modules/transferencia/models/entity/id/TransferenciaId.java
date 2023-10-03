package br.com.backend.modules.transferencia.models.entity.id;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaId {
    private UUID uuid;
    private EmpresaEntity empresa;
}
