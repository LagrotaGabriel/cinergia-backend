package br.com.backend.models.dto.cartao.request;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequest {
    private String nomePortador;
    private String cpfCnpjPortador;
    private Long numero;
    private Integer mesExpiracao;
    private Integer anoExpiracao;
    private Integer ccv;
}
