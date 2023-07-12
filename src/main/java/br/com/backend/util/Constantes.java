package br.com.backend.util;

public class Constantes {

    Constantes() {
    }

    public static final String ZERO_REAIS = "R$ 0,00";
    public static final String UM_REAL = "R$ 1,00";
    public static final String ERRO_CRIACAO_ASSINATURA_ASAAS = "Ocorreu um erro no processo de criação da assinatura: ";
    public static final String ERRO_CANCELAMENTO_ASSINATURA_ASAAS = "Ocorreu um erro no processo de cancelamento da assinatura: ";
    public static final String ERRO_CANCELAMENTO_PAGAMENTO_ASAAS = "Ocorreu um erro no processo de cancelamento do pagamento: ";
    public static final String ERRO_REMOCAO_CLIENTE_ASAAS = "Ocorreu um erro no processo de remoção do cliente: ";
    public static final String ERRO_CRIACAO_CLIENTE_ASAAS = "Ocorreu um erro no processo de criação do cliente na integradora: ";
    public static final String ERRO_CRIACAO_TRANSFERENCIA_ASAAS = "Ocorreu um erro no processo de criação de transferência na integradora: ";
    public static final String CONVERSAO_DE_TIPAGEM_COM_SUCESSO = "Conversão de tipagem realizada com sucesso";
    public static final String RETORNO_INTEGRADORA_NULO = "O retorno da integradora é nulo";

    public static final String ATUALIZANDO_VARIAVEIS_PAGAMENTO = "Atualizando variáveis do objeto pagamento...";

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

    public static final String PAGAMENTO_ENCONTRADO =
            "Pagamento encontrado: {}";

    public static final String TRANSFERENCIA_ENCONTRADA =
            "Transferência encontrada: {}";

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

    public static final String UTF_8 = "UTF-8";

    // E-MAIL
    public static final String INICIA_SERVICO_ENVIO_EMAILS =
            "Iniciando acesso ao serviço de envio de e-mails...";
    public static final String INICIANDO_ACESSO_OBTENCAO_CLIENT_AWS_SES =
            "Iniciando acesso ao método de obtenção de client da Subsy na AWS SES...";
    public static final String INICIANDO_CONSTRUCAO_OBJETO_REQUEST_EMAIL =
            "Iniciando construção do objeto SendEmailRequest, que será responsável por criar o corpo do e-mail...";
    public static final String CORPO_EMAIL_CRIADO =
            "Corpo do e-mail criado com sucesso...";
    public static final String INICIANDO_ENVIO_EMAIL =
            "Iniciando método de envio do e-mail através do AWS SES...";
    public static final String METODO_GERAR_HTML_EMAIL_ACESSADO = "Método responsável por gerar HTML do e-mail acessado";
    public static final String INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL =
            "Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...";
    public static final String DEFAULT_LOWER_CASE_PARAGRAPH = "<p style='padding: 0 1rem; color: #19191d; margin: 0'>";
    public static final String OPEN_DIV_FULL_WIDTH = "<div style='width: 100%;'>";
    public static final String CLOSE_DIV = "</div>";
    public static final String CLOSE_PARAGRAPH = "</p>";
    public static final String TITLE_H3 =
            "<h3 style='font-size: 1rem; color: #15161a; font-weight: 700; padding: 0 1rem;margin-top: 1rem;'>";
    public static final String COLORED_TITLE_DIV_CONTAINER =
            "<div style='width: 100%; padding: 0rem 1rem;background-color: #4a47d1; display: flex; justify-content: center'>";
    public static final String COLORED_TITLE_H3 =
            "<h3 style='font-size: 1.2rem; color: #C3C8C8; font-weight: 600'>";
    public static final String CLOSE_H3 = "</h3>";
    public static final String TITLE_H3_OLA = "Olá, ";
    public static final String OPEN_HTML = "<html>";
    public static final String CLOSE_HTML = "</html>";
    public static final String OPEN_HEAD = "<head>";
    public static final String CLOSE_HEAD = "</head>";
    public static final String OPEN_TITLE = "<title>";
    public static final String CLOSE_TITLE = "</title>";
    public static final String BUTTON_CONTAINER = "<div style='padding: 0 1rem'>";
    public static final String OPEN_BUTTON =
            "<button style='padding: 0.5rem 1rem; background-color: #4a47d1; border: 1px solid #4a47d1; border-radius: 3px'>";
    public static final String CLOSE_BUTTON = "</button>";
    public static final String OPEN_BODY = "<body>";
    public static final String CLOSE_BODY = "</body>";
    public static final String LINE_BREAK = "<br/>";
    public static final String DEFAULT_PARAGRAPH = "<p style='padding: 0 1rem; color: #19191d'>";
    public static final String BOLD_PARAGRAPH = "<p style='padding: 0 1rem; font-weight: 700; color: #19191d'>";
    public static final String SUBSY_GESTAO_ASSINATURAS_RECORRENTES =
            "Subsy - gestão de assinaturas e cobranças recorrentes";

    public static final String DIV_FOOTER =
            "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>";

    public static final String OPEN_ANCHOR = "<a style='font-size: 0.9rem; color: #FFF;text-decoration: none;' href='";
    public static final String CLOSE_ANCHOR = "</a>";

    public static final String OU_ACESSE = "Ou acesse ";
    public static final String ATENCIOSAMENTE = "Atenciosamente,";

    public static final String TELEFONE_SUBSY = "(11) 97981-5415";
    public static final String PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY =
            "<p style='width: 100%; display: flex; justify-content: center; font-size: 0.7rem; color: #272930; font-weight: 600;'>" +
                    "Este e-mail está sendo enviado pela Subsy - Gestão de assinaturas recorrentes";

    public static final String HTML_EMAIL_CRIADO =
            "HTML do e-mail criado com sucesso. Retornando valor...";

}
