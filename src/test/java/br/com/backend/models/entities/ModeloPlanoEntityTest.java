package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.ModeloPlanoEntityBuilder;
import br.com.backend.models.enums.CicloCobrancaEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Entity: ModeloPlano")
class ModeloPlanoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "ModeloPlanoEntity(id=1, descricaoPlano=Plano teste, valor=100.0, cicloCobranca=MENSAL)",
                ModeloPlanoEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {

        ModeloPlanoEntity modeloPlanoEntity = new ModeloPlanoEntity(
                1L,
                "Plano teste",
                100.0,
                CicloCobrancaEnum.MENSAL
        );

        Assertions.assertEquals(
                "ModeloPlanoEntity(id=1, descricaoPlano=Plano teste, valor=100.0, cicloCobranca=MENSAL)",
                modeloPlanoEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {

        ModeloPlanoEntity modeloPlanoEntity = ModeloPlanoEntity.builder()
                .id(1L)
                .descricaoPlano("Plano teste")
                .valor(100.0)
                .cicloCobranca(CicloCobrancaEnum.MENSAL)
                .build();

        Assertions.assertEquals(
                "ModeloPlanoEntity(id=1, descricaoPlano=Plano teste, valor=100.0, cicloCobranca=MENSAL)",
                modeloPlanoEntity.toString()
        );
    }

}