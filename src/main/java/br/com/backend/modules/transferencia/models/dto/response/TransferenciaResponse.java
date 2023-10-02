package br.com.backend.modules.transferencia.models.dto.response;

import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaResponse {
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private Double valor;
    private String chavePix;
    private TipoChavePixEnum tipoChavePix;
    private StatusTransferenciaEnum statusTransferencia;
}