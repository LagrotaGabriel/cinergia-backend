package br.com.backend.models.entities.global;

import br.com.backend.models.entities.global.mock.EnderecoEntityBuilder;
import br.com.backend.globals.models.endereco.enums.EstadoEnum;
import br.com.backend.globals.models.endereco.entity.EnderecoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Entity: Endereco")
class EnderecoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "EnderecoEntity(id=1, logradouro=Avenida Coronel Manuel Py, numero=583, " +
                        "bairro=Lauzane Paulista, codigoPostal=02442-090, cidade=São Paulo, complemento=Casa 4, " +
                        "estado=SP)",
                EnderecoEntityBuilder.builder().build().toString()
        );
    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        EnderecoEntity enderecoEntity = new EnderecoEntity(
                1L,
                "Rua 9",
                544,
                "Lauzane Paulista",
                "02442-050",
                "São Paulo",
                "Casa 4",
                EstadoEnum.SP
        );
        Assertions.assertEquals(
                "EnderecoEntity(id=1, logradouro=Rua 9, numero=544, bairro=Lauzane Paulista, " +
                        "codigoPostal=02442-050, cidade=São Paulo, complemento=Casa 4, estado=SP)",
                enderecoEntity.toString()
        );
    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        EnderecoEntity enderecoEntity = EnderecoEntity.builder()
                .id(1L)
                .logradouro("Rua Said Saad")
                .numero(145)
                .bairro("Lauzane Paulista")
                .codigoPostal("02324-312")
                .cidade("São Paulo")
                .estado(EstadoEnum.SP)
                .build();

        Assertions.assertEquals(
                "EnderecoEntity(id=1, logradouro=Rua Said Saad, numero=145, bairro=Lauzane Paulista, " +
                        "codigoPostal=02324-312, cidade=São Paulo, complemento=null, estado=SP)",
                enderecoEntity.toString()
        );
    }

}

