package br.com.backend.modules.transferencia.services.utils;

import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferenciaServiceUtil {

    TransferenciaServiceUtil() {
    }

    @Value("${spring.profiles.active}")
    String activeProfile;

    public String realizaTratamentoChavePix(String chavePix) {
        return chavePix
                .replace(".", "")
                .replace("-", "")
                .replace("/", "")
                .replace(",", "");
    }

    public void realizaSetagemDeChaveEvpParaAmbienteDeDesenvolvimento(TransferenciaRequest transferenciaRequest) {
        log.info("Método responsável por verificar ambiente de execução atual e realizar adaptações caso o " +
                "ambiente seja de desenvolvimento acessado");

        log.info("Verificando ambiente...");
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            log.info("Ambiente de desenvolvimento detectado. Iniciando setagem da chave PIX EVP...");
            transferenciaRequest.setChavePix("8512895b-99d1-4e3e-ac20-ba5e811f14b9");
            transferenciaRequest.setTipoChavePix(TipoChavePixEnum.EVP);
            log.info("Chave PIX EVP setada com sucesso");
        }
        else log.info("O ambiente atual não é de desenvolvimento. Nenhuma ação é necessária");
    }

}
