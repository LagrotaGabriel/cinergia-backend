package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.PagamentoEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Entity: Pagamento")
class PagamentoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "PagamentoEntity(id=1, idEmpresaResponsavel=null, idPlanoResponsavel=null, " +
                        "idClienteResponsavel=null, idAsaas=null, dataCadastro=2023-02-03, horaCadastro=10:57, " +
                        "dataPagamento=null, horaPagamento=null, valorBruto=650.0, taxaTotal=null, " +
                        "valorLiquidoAsaas=null, descricao=Assinatura de plano Basic, dataVencimento=2023-02-03, " +
                        "linkCobranca=null, linkBoletoAsaas=null, linkComprovante=null, formaPagamento=PIX, " +
                        "statusPagamento=APROVADO)",
                PagamentoEntityBuilder.builder().build().toString()
        );

    }

    @Test
    @DisplayName("Deve testar @AllArgsConstructor")
    void deveTestarAllArgsConstructor() {
        PagamentoEntity pagamentoEntity = new PagamentoEntity(
                1L,
                1L,
                1L,
                1L,
                "123",
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(15, 50).toString(),
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(15, 50).toString(),
                650.0,
                650.0,
                650.0,
                "Assinatura de plano Pro",
                LocalDate.of(2023, 2, 5).toString(),
                "",
                "",
                "",
                null,
                null
        );

        Assertions.assertEquals(
                "PagamentoEntity(id=1, idEmpresaResponsavel=1, idPlanoResponsavel=1, idClienteResponsavel=1, " +
                        "idAsaas=123, dataCadastro=2023-02-03, horaCadastro=15:50, dataPagamento=2023-02-03, " +
                        "horaPagamento=15:50, valorBruto=650.0, taxaTotal=650.0, valorLiquidoAsaas=650.0, " +
                        "descricao=Assinatura de plano Pro, dataVencimento=2023-02-05, linkCobranca=, " +
                        "linkBoletoAsaas=, linkComprovante=, formaPagamento=null, statusPagamento=null)",
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
                .descricao("Assinatura de plano Pro")
                .dataVencimento(LocalDate.of(2023, 2, 5).toString())
                .formaPagamento(null)
                .statusPagamento(null)
                .build();

        Assertions.assertEquals(
                "PagamentoEntity(id=1, idEmpresaResponsavel=null, idPlanoResponsavel=null, " +
                        "idClienteResponsavel=null, idAsaas=null, dataCadastro=2023-02-03, horaCadastro=15:50, " +
                        "dataPagamento=null, horaPagamento=null, valorBruto=650.0, taxaTotal=null, " +
                        "valorLiquidoAsaas=null, descricao=Assinatura de plano Pro, dataVencimento=2023-02-05, " +
                        "linkCobranca=null, linkBoletoAsaas=null, linkComprovante=null, formaPagamento=null, " +
                        "statusPagamento=null)",
                pagamentoEntity.toString()
        );
    }

}