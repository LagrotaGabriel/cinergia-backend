package br.com.backend.modules.cliente.proxy.models.response;

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
