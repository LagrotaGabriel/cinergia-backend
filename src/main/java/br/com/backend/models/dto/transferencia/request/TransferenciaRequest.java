package br.com.backend.models.dto.transferencia.request;

import br.com.backend.models.enums.TipoChavePixEnum;
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