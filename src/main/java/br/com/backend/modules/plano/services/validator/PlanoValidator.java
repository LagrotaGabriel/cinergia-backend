package br.com.backend.modules.plano.services.validator;

import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.services.validator.date.DateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class PlanoValidator {

    @Autowired
    DateValidator dateValidator;

    public void realizaValidacaoCriacaoNovoPlano(PlanoRequest planoRequest) {
        log.info("Método responsável pela validação de objeto PlanoRequest da criação de um novo plano acessado");

        if (StringUtils.hasText(planoRequest.getDataInicio()))
            dateValidator.deveValidarAtributoDataInicio(planoRequest.getDataInicio());

        log.info("Validações do objeto PlanoRequest realizadas com sucesso");
    }

}
