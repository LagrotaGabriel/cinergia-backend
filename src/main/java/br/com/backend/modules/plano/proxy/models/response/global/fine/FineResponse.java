package br.com.backend.modules.plano.proxy.models.response.global.fine;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineResponse {
    private Double value;
}
