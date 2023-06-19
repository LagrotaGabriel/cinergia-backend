package br.com.backend.proxy.plano.response.fine;

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
