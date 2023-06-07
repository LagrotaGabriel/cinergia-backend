package br.com.backend.models.entities.mock;

import br.com.backend.models.entities.ModeloPlanoEntity;
import br.com.backend.models.enums.CicloCobrancaEnum;

public class ModeloPlanoEntityBuilder {
    ModeloPlanoEntityBuilder() {
    }

    ModeloPlanoEntity modeloPlanoEntity;

    public static ModeloPlanoEntityBuilder builder() {
        ModeloPlanoEntityBuilder builder = new ModeloPlanoEntityBuilder();
        builder.modeloPlanoEntity = new ModeloPlanoEntity();
        builder.modeloPlanoEntity.setId(1L);
        builder.modeloPlanoEntity.setDescricaoPlano("Plano teste");
        builder.modeloPlanoEntity.setValor(100.0);
        builder.modeloPlanoEntity.setCicloCobranca(CicloCobrancaEnum.MENSAL);
        return builder;
    }

    public ModeloPlanoEntity build() {
        return modeloPlanoEntity;
    }
}
