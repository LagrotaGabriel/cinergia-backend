package br.com.backend.proxy.transferencia.response;

import br.com.backend.models.enums.TipoChavePixEnum;
import br.com.backend.proxy.transferencia.enums.StatusChavePixAsaas;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriaChavePixAsaasResponse {
    private String id;
    private String key;
    private TipoChavePixEnum type;
    private StatusChavePixAsaas status;
    private String dateCreated;
    private Boolean canBeDeleted;
    private String cannotBeDeletedReason;
    private QrCodeAsaas qrCode;
}
