package br.com.backend.modules.email.utils;

public class ConstantesEmail {

    ConstantesEmail() {}

    public static final String UTF_8 = "UTF-8";
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
