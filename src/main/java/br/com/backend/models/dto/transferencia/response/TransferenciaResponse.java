package br.com.backend.models.dto.transferencia.response;

import br.com.backend.models.enums.StatusTransferenciaEnum;
import br.com.backend.models.enums.TipoChavePixEnum;
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