package br.com.backend.proxy.webhooks.transferencia;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private String code;
    private String name;
    private String ispb;
}
