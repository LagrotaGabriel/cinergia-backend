package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.PlanoEntityBuilder;
import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
@DisplayName("Entity: Plano")
class PlanoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, dataCadastro=2023-02-03, " +
                        "horaCadastro=10:57, descricao=Assinatura de plano Basic, valor=100.0, " +
                        "dataVencimento=2023-02-03, formaPagamento=BOLETO, statusPlano=INATIVO)",
                PlanoEntityBuilder.builder().build().toString()
        );
    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        PlanoEntity planoEntity = new PlanoEntity(
                1L,
                1L,
                1L,
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(10, 2).toString(),
                "Plano teste",
                100.0,
                LocalDate.of(2023, 2, 3).toString(),
                FormaPagamentoEnum.CARTAO_DEBITO,
                StatusPlanoEnum.INATIVO,
                new ArrayList<>()
        );
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, dataCadastro=2023-02-03, " +
                        "horaCadastro=10:02, descricao=Plano teste, valor=100.0, dataVencimento=2023-02-03, " +
                        "formaPagamento=CARTAO_DEBITO, statusPlano=INATIVO)",
                planoEntity.toString()
        );
    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {
        PlanoEntity planoEntity = PlanoEntity.builder()
                .id(1L)
                .idEmpresaResponsavel(1L)
                .idClienteResponsavel(1L)
                .dataCadastro(LocalDate.of(2023, 2, 3).toString())
                .horaCadastro(LocalTime.of(10, 3).toString())
                .descricao("Plano teste")
                .valor(100.0)
                .dataVencimento(LocalDate.of(2023, 2, 3).toString())
                .formaPagamento(FormaPagamentoEnum.CARTAO_CREDITO)
                .statusPlano(StatusPlanoEnum.INATIVO)
                .pagamentos(new ArrayList<>())
                .build();
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, dataCadastro=2023-02-03, " +
                        "horaCadastro=10:03, descricao=Plano teste, valor=100.0, dataVencimento=2023-02-03, " +
                        "formaPagamento=CARTAO_CREDITO, statusPlano=INATIVO)",
                planoEntity.toString()
        );
    }

}
