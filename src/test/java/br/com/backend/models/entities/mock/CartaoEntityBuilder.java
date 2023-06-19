package br.com.backend.models.entities.mock;

import br.com.backend.models.entities.CartaoEntity;
import br.com.backend.models.enums.BandeiraCartaoEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public class CartaoEntityBuilder {

    CartaoEntityBuilder () {}
    CartaoEntity cartaoEntity;

    public static CartaoEntityBuilder builder() {
        CartaoEntityBuilder builder = new CartaoEntityBuilder();
        builder.cartaoEntity = new CartaoEntity();
        builder.cartaoEntity.setId(1L);
        builder.cartaoEntity.setDataCadastro(LocalDate.of(2023, 6, 2).toString());
        builder.cartaoEntity.setHoraCadastro(LocalTime.of(10, 29, 0).toString());
        builder.cartaoEntity.setNomePortador("Gabriel");
        builder.cartaoEntity.setCpfCnpjPortador("47153427821");
        builder.cartaoEntity.setNumero(5162306219378829L);
        builder.cartaoEntity.setCcv(318);
        builder.cartaoEntity.setMesExpiracao(8);
        builder.cartaoEntity.setAnoExpiracao(2025);
        builder.cartaoEntity.setTokenCartao(null);
        builder.cartaoEntity.setBandeiraCartaoEnum(BandeiraCartaoEnum.VISA);
        builder.cartaoEntity.setExclusao(null);
        return builder;
    }

    public CartaoEntity build() {
        return cartaoEntity;
    }

}
