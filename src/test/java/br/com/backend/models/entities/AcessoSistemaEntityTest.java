package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.AcessoSistemaEntityBuilder;
import br.com.backend.models.enums.PerfilEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@DisplayName("Entity: AcessoSistema")
class AcessoSistemaEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "AcessoSistemaEntity(id=1, senha=123, perfis=[])",
                AcessoSistemaEntityBuilder.builder().build().toString()
        );
    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {

        Set<PerfilEnum> perfilEnumHashSet = new HashSet<>();

        AcessoSistemaEntity acessoSistemaEntity = new AcessoSistemaEntity(
                1L,
                "123",
                perfilEnumHashSet
        );

        Assertions.assertEquals(
                "AcessoSistemaEntity(id=1, senha=123, perfis=[])",
                acessoSistemaEntity.toString()
        );
    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        Set<PerfilEnum> perfis = new HashSet<>();
        AcessoSistemaEntity acessoSistemaEntity = AcessoSistemaEntity.builder()
                .id(1L)
                .senha("123")
                .perfis(perfis)
                .build();
        Assertions.assertEquals(
                "AcessoSistemaEntity(id=1, senha=123, perfis=[])",
                acessoSistemaEntity.toString()
        );
    }

}
