package br.com.backend.proxy.webhooks.cobranca;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterestWebHook {
    private Double value;
    private String type;
}
