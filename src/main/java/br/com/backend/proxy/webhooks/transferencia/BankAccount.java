package br.com.backend.proxy.webhooks.transferencia;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private Bank bank;
    private String accountName;
    private String ownerName;
    private String cpfCnpj;
    private String type;
    private String agency;
    private String agencyDigit;
    private String account;
    private String accountDigit;
    private String pixAddressKey;
}
