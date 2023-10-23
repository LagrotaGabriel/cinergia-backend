package br.com.backend.modules.transferencia.models.dto.response;

import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaResponse {
    private UUID uuid;
    private String dataCadastro;
    private String horaCadastro;
    private String descricao;
    private Double valor;
    private String chavePix;
    private String tipoChavePix;
    private String statusTransferencia;

    public TransferenciaResponse constroiTransferenciaResponse(TransferenciaEntity transferenciaEntity) {
        log.info("Método de conversão de objeto do tipo TransferenciaEntity para objeto do tipo TransferenciaResponse acessado");

        log.info("Iniciando construção do objeto TransferenciaResponse...");
        TransferenciaResponse transferenciaResponse = TransferenciaResponse.builder()
                .uuid(transferenciaEntity.getUuid())
                .dataCadastro(transferenciaEntity.getDataCadastro())
                .horaCadastro(transferenciaEntity.getHoraCadastro())
                .descricao(transferenciaEntity.getDescricao())
                .valor(transferenciaEntity.getValor())
                .chavePix(transferenciaEntity.getChavePix())
                .tipoChavePix(transferenciaEntity.getTipoChavePix().getDesc())
                .statusTransferencia(transferenciaEntity.getStatusTransferencia().getDesc())
                .build();
        log.debug("Objeto TransferenciaResponse buildado com sucesso. Retornando...");
        return transferenciaResponse;
    }
}