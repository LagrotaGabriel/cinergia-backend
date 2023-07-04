package br.com.backend.proxy.transferencia.request;

import br.com.backend.models.enums.TipoChavePixEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriaChavePixAsaasRequest {
    TipoChavePixEnum type;
}
