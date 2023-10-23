package br.com.backend.globals.models.imagem.utils;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.globals.models.imagem.enums.TipoImagemEnum;

public class ImagemUtils {

    ImagemUtils() {}

    public static TipoImagemEnum realizaTratamentoTipoDeImagem(String tipoImagem) {
        if (tipoImagem.equals("image/png")) {
            return TipoImagemEnum.PNG;
        }
        else if (tipoImagem.equals("image/jpeg")) {
            return TipoImagemEnum.JPG;
        }
        else {
            throw new InvalidRequestException("O arquivo enviado deve ser uma imagem");
        }

    }
}
