package br.com.backend.modules.plano.proxy.models.request.global.fine;

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
