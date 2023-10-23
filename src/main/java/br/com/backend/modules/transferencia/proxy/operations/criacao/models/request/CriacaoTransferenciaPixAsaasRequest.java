package br.com.backend.modules.transferencia.proxy.operations.criacao.models.request;

import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriacaoTransferenciaPixAsaasRequest {
    private Double value;
    private String pixAddressKey;
    private TipoChavePixEnum pixAddressKeyType;
    private String description;
    private String scheduleDate;
}
