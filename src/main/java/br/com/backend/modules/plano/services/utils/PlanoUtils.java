package br.com.backend.modules.plano.services.utils;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlanoUtils {

    public void validaSePlanoEstaExcluido(PlanoEntity planoEntity, String mensagemCasoEstejaExcluido) {
        log.info("Método de validação de planoEntity excluído acessado");
        if (planoEntity.getStatusPlano().equals(StatusPlanoEnum.REMOVIDO)) {
            log.info("Plano de id {}: Validação de planoEntity já excluído falhou. Não é possível realizar operações " +
                    "em um planoEntity que já foi excluído.", planoEntity.getUuid());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.info("O planoEntity de id {} não está excluído", planoEntity.getUuid());
    }

}
