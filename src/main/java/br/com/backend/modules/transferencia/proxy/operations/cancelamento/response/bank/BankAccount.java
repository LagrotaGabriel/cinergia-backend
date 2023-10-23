package br.com.backend.modules.transferencia.proxy.operations.cancelamento.response.bank;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
        private Bank bank;
        private String accountName;
        private String ownerName;
        private String cpfCnpj;
        private String agency;
        private String agencyDigit;
        private String account;
        private String accountDigit;
}
