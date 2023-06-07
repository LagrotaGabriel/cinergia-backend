package br.com.backend.util;

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
}
