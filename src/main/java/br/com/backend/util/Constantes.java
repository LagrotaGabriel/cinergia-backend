package br.com.backend.util;

public class Constantes {

    Constantes() {
    }

    public static final String ERRO_CRIACAO_ASSINATURA_ASAAS = "Ocorreu um erro no processo de criação da assinatura: ";
    public static final String VALOR_RETORNADO_INTEGRADORA_NULO = "O valor retornado pela integradora na criação da subconta é nulo";
    public static final String ERRO_CRIACAO_CLIENTE_ASAAS = "Ocorreu um erro no processo de criação do cliente na integradora: ";
    public static final String CONVERSAO_DE_TIPAGEM_COM_SUCESSO = "Conversão de tipagem realizada com sucesso";

    public static final String INICIANDO_IMPL_PERSISTENCIA_CLIENTE =
            "Iniciando acesso ao método de implementação de persistência do cliente...";

    public static final String INICIANDO_IMPL_PERSISTENCIA_PLANO =
            "Iniciando acesso ao método de implementação de persistência do plano...";

    public static final String BUSCA_CLIENTE_POR_ID =
            "Iniciando acesso ao método de implementação de busca pelo cliente por id...";

    public static final String OBJETO_PAGAMENTO_CRIADO = "Objeto pagamento criado: {}";

    public static final String TOKEN_ASAAS = "TOKEN_ASAAS";

    public static final String FALHA_COMUNICACAO_ASAAS =
            "Ocorreu uma falha na comunicação com a integradora de pagamentos: ";

    public static final String PLANO_ENCONTRADO =
            "Plano encontrado: {}";

    public static final String INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS =
            "Iniciando acesso ao método de implementação de busca de pagamento por código ASAAS...";

    public static final String REMOVENDO_PAGAMENTO_DO_PLANO =
            "Removendo pagamento encontrado do objeto plano: {}";

    public static final String BUSCA_PAGINADA_PAGAMENTOS_SUCESSO =
            "A busca paginada de pagamentos foi realizada com sucesso";

    public static final String CONVERSAO_TIPAGEM_SUCESSO =
            "Conversão de tipagem realizada com sucesso";

    public static final String CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE =
            "Busca de pagamentos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                    "Entity para objetos do tipo Response...";

}
