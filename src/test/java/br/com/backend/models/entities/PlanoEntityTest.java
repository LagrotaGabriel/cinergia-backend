package br.com.backend.models.entities;

import br.com.backend.models.entities.mock.PlanoEntityBuilder;
import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Entity: Plano")
class PlanoEntityTest {

    @Test
    @DisplayName("Deve testar dataBuilder")
    void deveTestarDataBuilder() {
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, idAsaas=null, " +
                        "dataCadastro=2023-02-03, horaCadastro=10:57, dataVencimento=null, dataInicio=null, " +
                        "descricao=Assinatura de plano Basic, valor=100.0, formaPagamento=BOLETO, statusPlano=INATIVO, " +
                        "periodicidade=null, notificacoes=[], cartao=null)",
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
                "plano_123",
                LocalDate.of(2023, 2, 3).toString(),
                LocalTime.of(10, 2).toString(),
                LocalDate.of(2023, 2, 3).toString(),
                "2030-01-01",
                "Plano teste",
                100.0,
                FormaPagamentoEnum.DEBIT_CARD,
                StatusPlanoEnum.INATIVO,
                PeriodicidadeEnum.MENSAL,
                null,
                null,
                new ArrayList<>()
        );
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, idAsaas=plano_123, " +
                        "dataCadastro=2023-02-03, horaCadastro=10:02, dataVencimento=2023-02-03, dataInicio=2030-01-01, " +
                        "descricao=Plano teste, valor=100.0, formaPagamento=DEBIT_CARD, statusPlano=INATIVO, " +
                        "periodicidade=MENSAL, notificacoes=null, cartao=null)",
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
                .formaPagamento(FormaPagamentoEnum.CREDIT_CARD)
                .statusPlano(StatusPlanoEnum.INATIVO)
                .pagamentos(new ArrayList<>())
                .build();
        Assertions.assertEquals(
                "PlanoEntity(id=1, idEmpresaResponsavel=1, idClienteResponsavel=1, idAsaas=null, " +
                        "dataCadastro=2023-02-03, horaCadastro=10:03, dataVencimento=null, dataInicio=null, " +
                        "descricao=Plano teste, valor=100.0, formaPagamento=CREDIT_CARD, statusPlano=INATIVO, " +
                        "periodicidade=null, notificacoes=null, cartao=null)",
                planoEntity.toString()
        );
    }

}
