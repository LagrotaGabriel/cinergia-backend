package br.com.backend.modules.plano.proxy.global.request.fine;

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
