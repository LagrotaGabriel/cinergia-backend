package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.CartaoEntityBuilder;
import br.com.backend.models.enums.BandeiraCartaoEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@DisplayName("Entity: Cartao")
class CartaoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "CartaoEntity(id=1, dataCadastro=2023-06-02, horaCadastro=10:29, ativo=true, " +
                        "nomePortador=Gabriel, cpfCnpj=47153427821, numero=5162306219378829, cvv=318, " +
                        "mesExpiracao=8, anoExpiracao=2025, tokenCartao=null, bandeiraCartaoEnum=VISA, exclusao=null)",
                CartaoEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        CartaoEntity cartaoEntity = new CartaoEntity(
                1L,
                LocalDate.of(2023, 6, 2).toString(),
                LocalTime.of(10, 48, 0).toString(),
                "Fulano",
                "12345678910",
                5162306219378829L,
                315,
                4,
                24,
                "8f037c5a-20ec-48b5-96e3-412e1f4c4367",
                BandeiraCartaoEnum.HIPERCARD,
                null
        );
        Assertions.assertEquals(
                "CartaoEntity(id=1, dataCadastro=2023-06-02, horaCadastro=10:48, ativo=true, " +
                        "nomePortador=Fulano, cpfCnpj=12345678910, numero=5162306219378829, cvv=315, mesExpiracao=4, " +
                        "anoExpiracao=24, tokenCartao=8f037c5a-20ec-48b5-96e3-412e1f4c4367, " +
                        "bandeiraCartaoEnum=HIPERCARD, exclusao=null)",
                cartaoEntity.toString()
        );

    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        CartaoEntity cartaoEntity = CartaoEntity.builder()
                .id(1L)
                .dataCadastro(LocalDate.of(2023, 6, 2).toString())
                .horaCadastro(LocalTime.of(10, 48, 0).toString())
                .nomePortador("Fulano")
                .cpfCnpjPortador("12345678910")
                .numero(5162306219378829L)
                .ccv(310)
                .mesExpiracao(2)
                .anoExpiracao(27)
                .tokenCartao(null)
                .bandeiraCartaoEnum(BandeiraCartaoEnum.HIPERCARD)
                .build();
        Assertions.assertEquals(
                "CartaoEntity(id=1, dataCadastro=2023-06-02, horaCadastro=10:48, ativo=false, " +
                        "nomePortador=Fulano, cpfCnpj=12345678910, numero=5162306219378829, cvv=310, mesExpiracao=2, " +
                        "anoExpiracao=27, tokenCartao=null, bandeiraCartaoEnum=HIPERCARD, exclusao=null)",
                cartaoEntity.toString()
        );
    }

}