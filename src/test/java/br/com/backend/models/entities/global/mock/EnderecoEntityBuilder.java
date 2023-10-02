package br.com.backend.models.entities.global.mock;

import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import br.com.backend.globals.models.endereco.enums.EstadoEnum;

public class EnderecoEntityBuilder {
    EnderecoEntityBuilder(){}
    EnderecoEntity enderecoEntity;

    public static EnderecoEntityBuilder builder() {
        EnderecoEntityBuilder builder = new EnderecoEntityBuilder();
        builder.enderecoEntity = new EnderecoEntity();
        builder.enderecoEntity.setId(1L);
        builder.enderecoEntity.setLogradouro("Avenida Coronel Manuel Py");
        builder.enderecoEntity.setNumero(583);
        builder.enderecoEntity.setCodigoPostal("02442-090");
        builder.enderecoEntity.setBairro("Lauzane Paulista");
        builder.enderecoEntity.setCidade("São Paulo");
        builder.enderecoEntity.setComplemento("Casa 4");
        builder.enderecoEntity.setEstado(EstadoEnum.SP);
        return builder;
    }

    public EnderecoEntity build() {
        return enderecoEntity;
    }
}
