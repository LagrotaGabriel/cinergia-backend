package br.com.backend.models.entities.mock;

import br.com.backend.models.entities.ClienteEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ClienteEntityBuilder {
    ClienteEntityBuilder() {
    }

    ClienteEntity clienteEntity;

    public static ClienteEntityBuilder builder() {
        ClienteEntityBuilder builder = new ClienteEntityBuilder();
        builder.clienteEntity = new ClienteEntity();

        builder.clienteEntity.setId(1L);
        builder.clienteEntity.setDataCadastro(LocalDate.of(2023, 2, 3).toString());
        builder.clienteEntity.setHoraCadastro(LocalTime.of(10, 40).toString());
        builder.clienteEntity.setNome("Fulano");
        builder.clienteEntity.setEmail("fulano@gmail.com");
        builder.clienteEntity.setDataNascimento("2023-02-03");
        builder.clienteEntity.setExclusao(null);
        builder.clienteEntity.setTelefone(null);
        builder.clienteEntity.setEndereco(null);
        builder.clienteEntity.setPlanos(new ArrayList<>());
        builder.clienteEntity.setCartoes(new ArrayList<>());
        return builder;
    }

    public ClienteEntity build() {
        return clienteEntity;
    }

}
