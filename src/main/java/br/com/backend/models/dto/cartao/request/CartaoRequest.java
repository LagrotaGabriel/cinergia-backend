package br.com.backend.models.dto.cartao.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequest {
    @NotNull
    private String nomePortador;
    @NotNull
    private String cpfCnpjPortador;
    @NotNull
    private Long numero;
    @NotNull
    private Integer mesExpiracao;
    @NotNull
    private Integer anoExpiracao;
    @NotNull
    private Integer ccv;
}
