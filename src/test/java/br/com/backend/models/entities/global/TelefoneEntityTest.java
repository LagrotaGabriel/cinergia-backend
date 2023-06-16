package br.com.backend.models.entities.global;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Entity: Telefone")
class TelefoneEntityTest {

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {

        TelefoneEntity telefoneEntity =
                new TelefoneEntity(1L, "11", "979815415");

        Assertions.assertEquals(
                "TelefoneEntity(id=1, prefixo=11, numero=979815415)",
                telefoneEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        TelefoneEntity telefoneEntity = TelefoneEntity.builder()
                .id(1L)
                .prefixo("11")
                .numero("979815415")
                .build();
        Assertions.assertEquals(
                "TelefoneEntity(id=1, prefixo=11, numero=979815415)",
                telefoneEntity.toString()
        );
    }

}

