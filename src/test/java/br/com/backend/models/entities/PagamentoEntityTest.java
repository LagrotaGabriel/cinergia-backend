package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.PagamentoEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@DisplayName("Entity: Pagamento")
class PagamentoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "PagamentoEntity(id=1, dataCadastro=2023-02-03, horaCadastro=10:57, valorBruto=650.0, " +
                        "valorTaxaIntegradora=20.0, valorTaxaSistema=20.0, descricao=Assinatura de plano Basic, " +
                        "dataVencimento=2023-02-03, finalCartao=123, formaPagamento=PIX, statusPagamento=APROVADO)",
                PagamentoEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        PagamentoEntity pagamentoEntity = new PagamentoEntity(
                1L,
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(15, 50).toString(),
                650.0,
                640.0,
                640.0,
                "Assinatura de plano Pro",
                LocalDate.of(2023, 2, 5).toString(),
                "123",
                null,
                null
        );

        Assertions.assertEquals(
                "PagamentoEntity(id=1, dataCadastro=2023-02-03, horaCadastro=15:50, valorBruto=650.0, " +
                        "valorTaxaIntegradora=640.0, valorTaxaSistema=640.0, descricao=Assinatura de plano Pro, " +
                        "dataVencimento=2023-02-05, finalCartao=123, formaPagamento=null, statusPagamento=null)",
                pagamentoEntity.toString()
        );
    }

    @Test
    @DisplayName("Deve testar @Builder")
    void deveTestarBuilder() {

        PagamentoEntity pagamentoEntity = PagamentoEntity.builder()
                .id(1L)
                .dataCadastro(LocalDate.of(2023, 2, 3).toString())
                .horaCadastro(LocalTime.of(15, 50).toString())
                .valorBruto(650.0)
                .valorTaxaIntegradora(640.0)
                .valorTaxaSistema(640.0)
                .descricao("Assinatura de plano Pro")
                .dataVencimento(LocalDate.of(2023, 2, 5).toString())
                .finalCartao("123")
                .formaPagamento(null)
                .statusPagamento(null)
                .build();

        Assertions.assertEquals(
                "PagamentoEntity(id=1, dataCadastro=2023-02-03, horaCadastro=15:50, valorBruto=650.0, " +
                        "valorTaxaIntegradora=640.0, valorTaxaSistema=640.0, descricao=Assinatura de plano Pro, " +
                        "dataVencimento=2023-02-05, finalCartao=123, formaPagamento=null, statusPagamento=null)",
                pagamentoEntity.toString()
        );
    }

}