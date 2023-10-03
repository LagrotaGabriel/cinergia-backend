package br.com.backend.modules.pagamento.utils;

public class ConstantesPagamento {

    ConstantesPagamento() {
    }

    public static final String ERRO_CANCELAMENTO_PAGAMENTO_ASAAS = "Ocorreu um erro no processo de cancelamento do pagamento: ";
    public static final String ATUALIZANDO_VARIAVEIS_PAGAMENTO = "Atualizando variáveis do objeto pagamento...";
    public static final String OBJETO_PAGAMENTO_CRIADO = "Objeto pagamento criado: {}";
    public static final String PAGAMENTO_ENCONTRADO =
            "Pagamento encontrado: {}";
    public static final String INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS =
            "Iniciando acesso ao método de implementação de busca de pagamento por código ASAAS...";

    public static final String REMOVENDO_PAGAMENTO_DO_PLANO =
            "Removendo pagamento encontrado do objeto plano: {}";

    public static final String BUSCA_PAGINADA_PAGAMENTOS_SUCESSO =
            "A busca paginada de pagamentos foi realizada com sucesso";
    public static final String CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE =
            "Busca de pagamentos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                    "Entity para objetos do tipo Response...";

}
