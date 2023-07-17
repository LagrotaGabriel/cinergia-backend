package br.com.backend.proxy.plano.request.global.fine;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineRequest {
    private Double value;
}
