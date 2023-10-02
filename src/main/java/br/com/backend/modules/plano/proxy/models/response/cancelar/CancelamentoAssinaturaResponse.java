package br.com.backend.modules.plano.proxy.models.response.cancelar;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoAssinaturaResponse {
    private Boolean deleted;
    private String id;
}
