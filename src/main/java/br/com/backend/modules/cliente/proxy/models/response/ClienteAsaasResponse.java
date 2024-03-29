package br.com.backend.modules.cliente.proxy.models.response;

import br.com.backend.modules.cliente.models.enums.TipoPessoaEnum;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteAsaasResponse {

    private String object;
    private String id;
    private String dateCreated;
    private String name;
    private String email;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String cpfCnpj;
    private TipoPessoaEnum personType;
    private Boolean deleted;
    private String additionalEmails;
    private String externalReference;
    private Boolean notificationDisabled;
    private Integer city;
    private String state;
    private String country;
    private String observations;

}
