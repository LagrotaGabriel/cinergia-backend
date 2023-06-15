package br.com.backend.proxy.empresa.response;

import br.com.backend.models.enums.TipoPessoaEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubContaAsaasResponse {
    private String id;
    private String name;
    private String email;
    private String cpfCnpj;
    private String birthDate;
    private CompanyTypeEnum companyType;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private TipoPessoaEnum personType;
    private String city;
    private String state;
    private String country;
    private AccountNumber accountNumber;
    private String walletId;
    private String apiKey;
}
