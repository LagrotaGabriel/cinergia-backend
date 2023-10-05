package br.com.backend.modules.cliente.proxy.models.request;

import br.com.backend.globals.models.endereco.dto.request.EnderecoRequest;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.proxy.utils.ClienteAsaasProxyUtils;
import lombok.*;

import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteAsaasRequest {

    private String name;
    private String cpfCnpj;
    private String email;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String externalReference;
    private Boolean notificationDisabled;
    private String additionalEmails;
    private String municipalInscription;
    private String stateInscription;
    private String groupName;

    public ClienteAsaasRequest constroiObjetoCriaClienteAsaasRequest(UUID uuidEmpresaSessao,
                                                                     ClienteRequest clienteRequest) {

        EnderecoRequest endereco = clienteRequest.getEndereco();

        return ClienteAsaasRequest.builder()
                .name(clienteRequest.getNome())
                .cpfCnpj(clienteRequest.getCpfCnpj())
                .email(clienteRequest.getEmail())
                .phone(ClienteAsaasProxyUtils.obtemTelefoneFixoCliente(clienteRequest.getTelefones()))
                .mobilePhone(ClienteAsaasProxyUtils.obtemTelefoneCelularCliente(clienteRequest.getTelefones()))
                .address(endereco != null ? endereco.getLogradouro() : null)
                .addressNumber(endereco != null ? endereco.getNumero().toString() : null)
                .complement(endereco != null ? endereco.getComplemento() : null)
                .province(endereco != null ? endereco.getBairro() : null)
                .postalCode(endereco != null ? endereco.getCodigoPostal() : null)
                .externalReference(uuidEmpresaSessao.toString() + ';' + clienteRequest.getCpfCnpj())
                .notificationDisabled(true)
                .additionalEmails(null)
                .municipalInscription(null)
                .stateInscription(null)
                .groupName(uuidEmpresaSessao.toString())
                .build();
    }
}
