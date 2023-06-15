package br.com.backend.proxy.empresa.response;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountNumber {
    private String agency;
    private String account;
    private String accountDigit;
}
