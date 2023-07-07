package br.com.backend.proxy.cliente.response;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RemoveClienteAsaasResponse {
    private Boolean deleted;
    private String id;
}
