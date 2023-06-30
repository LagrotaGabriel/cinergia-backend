package br.com.backend.models.dto.empresa.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosDashBoardResponse {
    private String saldo;
    private String atrasado;
    private String previsto;
    private String confirmado;
    private Integer qtdAssinaturasAtivas;
    private Integer qtdAssinaturasInativas;
    private Integer totalAssinaturas;
}
