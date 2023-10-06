package br.com.backend.globals.models.telefone.response;

import br.com.backend.globals.models.telefone.entity.TelefoneEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneResponse {
    private String prefixo;
    private String numero;

    public TelefoneResponse constroiTelefoneResponse(TelefoneEntity telefoneEntity) {
        log.info("Método de conversão de objeto do tipo TelefoneEntity para objeto do tipo TelefoneResponse acessado");

        log.info("Iniciando construção do objeto TelefoneResponse...");
        TelefoneResponse telefoneResponse = TelefoneResponse.builder()
                .prefixo(telefoneEntity.getPrefixo())
                .numero(telefoneEntity.getNumero())
                .build();

        log.debug("Objeto TelefoneResponse buildado com sucesso. Retornando...");
        return telefoneResponse;
    }

    public List<TelefoneResponse> constroiListaTelefoneResponse(List<TelefoneEntity> telefonesEntity) {
        List<TelefoneResponse> telefonesResponse = new ArrayList<>();

        telefonesEntity.forEach(telefoneEntity ->
                telefonesResponse.add(constroiTelefoneResponse(telefoneEntity)));

        return telefonesResponse;
    }
}
