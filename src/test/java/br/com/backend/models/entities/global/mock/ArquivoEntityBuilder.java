package br.com.backend.models.entities.global.mock;

import br.com.backend.models.entities.global.ArquivoEntity;
import br.com.backend.models.enums.global.TipoArquivoEnum;

public class ArquivoEntityBuilder {
    ArquivoEntityBuilder() {
    }

    ArquivoEntity arquivoEntity;

    public static ArquivoEntityBuilder builder() {
        ArquivoEntityBuilder builder = new ArquivoEntityBuilder();
        builder.arquivoEntity = new ArquivoEntity();
        builder.arquivoEntity.setId(1L);
        builder.arquivoEntity.setNome("arquivo.pdf");
        builder.arquivoEntity.setTamanho(1000L);
        builder.arquivoEntity.setTipo(TipoArquivoEnum.PDF);
        builder.arquivoEntity.setArquivo(new byte[0]);
        return builder;
    }

    public ArquivoEntity build() {
        return arquivoEntity;
    }
}
