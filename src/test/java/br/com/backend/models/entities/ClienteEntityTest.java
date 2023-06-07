package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.ClienteEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
@DisplayName("Entity: ClienteEntity")
class ClienteEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "ClienteEntity(id=1, dataCadastro=2023-02-03, horaCadastro=10:40, nome=Fulano, " +
                        "email=fulano@gmail.com, dataNascimento=2023-02-03, exclusao=null, telefone=null, " +
                        "endereco=null)",
                ClienteEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        ClienteEntity clienteEntity = new ClienteEntity(
                1L,
                LocalDate.of(2023, 2, 27).toString(),
                LocalTime.of(17, 40).toString(),
                "Gabriel Lagrota",
                "gabrielafonso@mail.com.br",
                "1998-07-21",
                null,
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        Assertions.assertEquals(
                "ClienteEntity(id=1, dataCadastro=2023-02-27, horaCadastro=17:40, nome=Gabriel Lagrota, " +
                        "email=gabrielafonso@mail.com.br, dataNascimento=1998-07-21, exclusao=null, telefone=null, " +
                        "endereco=null)",
                clienteEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        ClienteEntity clienteEntity = ClienteEntity.builder()
                .id(1L)
                .dataCadastro(LocalDate.of(2023, 2, 27).toString())
                .horaCadastro(LocalTime.of(17, 40).toString())
                .nome("Gabriel Lagrota")
                .email("gabrielafonso@mail.com.br")
                .dataNascimento("1998-07-21")
                .exclusao(null)
                .telefone(null)
                .endereco(null)
                .planos(new ArrayList<>())
                .cartoes(new ArrayList<>())
                .build();
        Assertions.assertEquals(
                "ClienteEntity(id=1, dataCadastro=2023-02-27, horaCadastro=17:40, nome=Gabriel Lagrota, " +
                        "email=gabrielafonso@mail.com.br, dataNascimento=1998-07-21, exclusao=null, telefone=null, " +
                        "endereco=null)",
                clienteEntity.toString()
        );
    }

}