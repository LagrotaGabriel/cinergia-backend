package br.com.backend.proxy.plano.response.global.fine;

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
