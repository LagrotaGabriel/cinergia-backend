package br.com.backend.models.entities.mock;

import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;

import java.util.HashSet;

public class AcessoSistemaEntityBuilder {

    AcessoSistemaEntityBuilder() {
    }

    AcessoSistemaEntity acessoSistemaEntity;

    public static AcessoSistemaEntityBuilder builder() {
        AcessoSistemaEntityBuilder builder = new AcessoSistemaEntityBuilder();
        builder.acessoSistemaEntity = new AcessoSistemaEntity();
        builder.acessoSistemaEntity.setId(1L);
        builder.acessoSistemaEntity.setSenha("123");
        builder.acessoSistemaEntity.setPerfis(new HashSet<>());
        return builder;
    }

    public AcessoSistemaEntity build() {
        return acessoSistemaEntity;
    }

}
