package br.com.backend.models.entities.mock;

import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class PlanoEntityBuilder {
    PlanoEntityBuilder() {
    }

    PlanoEntity planoEntity;

    public static PlanoEntityBuilder builder() {
        PlanoEntityBuilder builder = new PlanoEntityBuilder();
        builder.planoEntity = new PlanoEntity();
        builder.planoEntity.setId(1L);
        builder.planoEntity.setIdEmpresaResponsavel(1L);
        builder.planoEntity.setIdClienteResponsavel(1L);
        builder.planoEntity.setDataCadastro(LocalDate.of(2023, 2, 3).toString());
        builder.planoEntity.setHoraCadastro(LocalTime.of(10, 57).toString());
        builder.planoEntity.setDescricao("Assinatura de plano Basic");
        builder.planoEntity.setValor(100.0);
        builder.planoEntity.setDataVencimento("2023-02-03");
        builder.planoEntity.setFormaPagamento(FormaPagamentoEnum.BOLETO);
        builder.planoEntity.setStatusPlano(StatusPlanoEnum.INATIVO);
        builder.planoEntity.setPagamentos(new ArrayList<>());
        return builder;
    }

    public PlanoEntity build() {
        return planoEntity;
    }
}
