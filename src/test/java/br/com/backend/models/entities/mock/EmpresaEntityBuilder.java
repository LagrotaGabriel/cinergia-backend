package br.com.backend.models.entities.mock;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EmpresaEntityBuilder {
    EmpresaEntityBuilder() {
    }

    EmpresaEntity empresaEntity;

    public static EmpresaEntityBuilder builder() {
        EmpresaEntityBuilder builder = new EmpresaEntityBuilder();
        builder.empresaEntity = new EmpresaEntity();

        builder.empresaEntity.setId(1L);
        builder.empresaEntity.setDataCadastro(LocalDate.of(2023, 2, 3).toString());
        builder.empresaEntity.setHoraCadastro(LocalTime.of(10, 48).toString());
        builder.empresaEntity.setNomeEmpresa("Akadia Solutions");
        builder.empresaEntity.setEmail("akadia@gmail.com");
        builder.empresaEntity.setCpfCnpj("12345678000112");
        builder.empresaEntity.setDataNascimento("1998-07-21");
        builder.empresaEntity.setEndereco(null);
        builder.empresaEntity.setTelefone(null);
        builder.empresaEntity.setAcessoSistema(null);
        builder.empresaEntity.setLogoEmpresa(null);
        builder.empresaEntity.setClientes(new ArrayList<>());
        return builder;
    }

    public EmpresaEntity build() {
        return empresaEntity;
    }
}
