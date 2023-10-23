package br.com.backend.globals.models.endereco.dto.response;

import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponse {
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String codigoPostal;
    private String cidade;
    private String complemento;
    private String estado;

    public EnderecoResponse constroiEnderecoResponse(EnderecoEntity enderecoEntity) {
        log.info("Método de conversão de objeto do tipo EnderecoEntity para objeto do tipo EnderecoResponse acessado");

        log.info("Iniciando construção do objeto EnderecoResponse...");
        EnderecoResponse enderecoResponse = EnderecoResponse.builder()
                .logradouro(enderecoEntity.getLogradouro())
                .numero(enderecoEntity.getNumero())
                .bairro(enderecoEntity.getBairro())
                .codigoPostal(enderecoEntity.getCodigoPostal())
                .cidade(enderecoEntity.getCidade())
                .complemento(enderecoEntity.getComplemento())
                .estado(enderecoEntity.getEstado().toString())
                .build();
        log.debug("Objeto EnderecoResponse buildado com sucesso. Retornando...");
        return enderecoResponse;
    }
}
