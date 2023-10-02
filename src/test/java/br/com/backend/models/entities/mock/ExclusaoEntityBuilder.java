package br.com.backend.models.entities.mock;

import br.com.backend.globals.models.exclusao.entity.ExclusaoEntity;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExclusaoEntityBuilder {
    ExclusaoEntityBuilder() {
    }

    ExclusaoEntity exclusaoEntity;

    public static ExclusaoEntityBuilder builder() {
        ExclusaoEntityBuilder builder = new ExclusaoEntityBuilder();
        builder.exclusaoEntity = new ExclusaoEntity();
        builder.exclusaoEntity.setId(1L);
        builder.exclusaoEntity.setDataExclusao(LocalDate.of(2023, 2, 13).toString());
        builder.exclusaoEntity.setHoraExclusao(LocalTime.of(10, 44).toString());
        return builder;
    }

    public ExclusaoEntity build() {
        return exclusaoEntity;
    }
}
