package br.com.backend.proxy.empresa.request;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubContaAsaasRequest {
    private String name;
    private String email;
    private String cpfCnpj;
    private String birthDate;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
}
