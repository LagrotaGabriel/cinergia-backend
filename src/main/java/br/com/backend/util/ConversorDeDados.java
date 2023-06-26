package br.com.backend.util;

import java.text.NumberFormat;
import java.util.Locale;

public class ConversorDeDados {
    ConversorDeDados() {
    }

    public static String converteDataUsParaDataBr(String dataUs) {
        String[] dataSplitada = dataUs.split("-");
        if (dataSplitada.length == 3) {
            return dataSplitada[2] + "/" + dataSplitada[1] + "/" + dataSplitada[0];
        }
        return dataUs;
    }

    public static String converteValorDoubleParaValorMonetario(Double valor) {
        Locale ptBr = new Locale("pt", "BR");
        return NumberFormat.getCurrencyInstance(ptBr).format(valor);
    }
}
