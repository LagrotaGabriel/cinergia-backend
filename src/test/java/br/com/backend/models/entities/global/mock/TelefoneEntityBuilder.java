package br.com.backend.models.entities.global.mock;

import br.com.backend.models.entities.global.TelefoneEntity;

public class TelefoneEntityBuilder {
    TelefoneEntityBuilder(){}
    TelefoneEntity telefoneEntity;

    public static TelefoneEntityBuilder builder() {
        TelefoneEntityBuilder builder = new TelefoneEntityBuilder();
        builder.telefoneEntity = new TelefoneEntity();
        builder.telefoneEntity.setId(1L);
        builder.telefoneEntity.setPrefixo("11");
        builder.telefoneEntity.setNumero("979815415");
        return builder;
    }

    public TelefoneEntity build() {
        return telefoneEntity;
    }
}
