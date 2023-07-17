package br.com.backend.proxy.plano.response.cancelar;

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
