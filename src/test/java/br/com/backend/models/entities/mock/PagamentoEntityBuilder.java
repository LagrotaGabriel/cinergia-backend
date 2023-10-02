package br.com.backend.models.entities.mock;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public class PagamentoEntityBuilder {
    PagamentoEntityBuilder() {
    }

    PagamentoEntity pagamentoEntity;

    public static PagamentoEntityBuilder builder() {
        PagamentoEntityBuilder builder = new PagamentoEntityBuilder();
        builder.pagamentoEntity = new PagamentoEntity();
        builder.pagamentoEntity.setId(1L);
        builder.pagamentoEntity.setDataCadastro(LocalDate.of(2023, 2, 3).toString());
        builder.pagamentoEntity.setHoraCadastro(LocalTime.of(10, 57).toString());
        builder.pagamentoEntity.setValorBruto(650.0);
        builder.pagamentoEntity.setDescricao("Assinatura de plano Basic");
        builder.pagamentoEntity.setDataVencimento("2023-02-03");
        builder.pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.APROVADO);
        builder.pagamentoEntity.setFormaPagamento(FormaPagamentoEnum.PIX);
        return builder;
    }

    public PagamentoEntity build() {
        return pagamentoEntity;
    }
}
