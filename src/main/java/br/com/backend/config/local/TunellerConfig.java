package br.com.backend.config.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TunellerConfig {

    @Value("${tuneller.path}")
    String caminhoLocalTunelamento;

    public void iniciaExposicaoDeRedeLocalParaIntegradoras() throws IOException {
        Runtime.getRuntime().exec(
                "cmd /c" + caminhoLocalTunelamento + "\\road-builder.bat",
                null,
                new File(caminhoLocalTunelamento));
    }

}
