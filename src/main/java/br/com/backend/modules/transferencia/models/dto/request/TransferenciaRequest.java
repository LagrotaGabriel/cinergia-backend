package br.com.backend.modules.transferencia.models.dto.request;

import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequest {
    private String descricao;
    private Double valor;
    private String chavePix;
    private TipoChavePixEnum tipoChavePix;
}