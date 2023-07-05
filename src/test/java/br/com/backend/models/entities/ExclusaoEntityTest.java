package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.ExclusaoEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@DisplayName("Entity: Exclusao")
class ExclusaoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "ExclusaoEntity(id=1, dataExclusao=2023-02-13, horaExclusao=10:44)", ExclusaoEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {

        ExclusaoEntity exclusaoEntity = new ExclusaoEntity(
                1L,
                LocalDate.of(2023, 6, 2).toString(),
                LocalTime.of(11, 3, 0).toString()
        );

        Assertions.assertEquals(
                "ExclusaoEntity(id=1, dataExclusao=2023-06-02, horaExclusao=11:03)", exclusaoEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {

        ExclusaoEntity exclusaoEntity = ExclusaoEntity.builder()
                .id(1L)
                .dataExclusao(LocalDate.of(2023, 6, 2).toString())
                .horaExclusao(LocalTime.of(11, 3, 0).toString())
                .build();

        Assertions.assertEquals(
                "ExclusaoEntity(id=1, dataExclusao=2023-06-02, horaExclusao=11:03)", exclusaoEntity.toString()
        );
    }

}
