package br.com.backend.services.email.services;

import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class GeradorBodyHtmlEmail {

    @Value("${EMAIL_SUBSY_AWS")
    String emailSubsyAws;

    public String geraBodyHtmlParaCobrancaAssinatura(PagamentoEntity pagamento,
                                                     PlanoEntity planoEntity,
                                                     ClienteEntity clienteEntity) {

        log.debug("Método responsável por gerar HTML do e-mail acessado");

        log.debug("Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...");
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));

        log.debug("Iniciando criação do corpo HTML do e-mail...");
        String htmlMsg =
                "<html>" +
                        "<head>" +
                        "<title>" +
                        "Sua fatura" +
                        "</title>" +
                        "</head>" +
                        "<body>" +
                        "<div style='width: 100%;'>" +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #4a47d1; display: flex; justify-content: center'>" +
                        "<h3 style='font-size: 1.2rem; color: #C3C8C8; font-weight: 600'>" +
                        "Cobrança gerada" +
                        "</h3>" +
                        Constantes.CLOSE_DIV +
                        "<h3 style='font-size: 1rem; color: #15161a; font-weight: 700; padding: 0 1rem;margin-top: 1rem;'>" +
                        "Olá, " + StringUtils.capitalize(clienteEntity.getNome()) +
                        "</h3>" +
                        "<p style='padding: 0 1rem'>" +
                        "O link de pagamento da sua assinatura <b>" + planoEntity.getDescricao().toUpperCase() + "</b> no valor de <b>" + valorCobrancaEmReais + "</b> com " +
                        "vencimento para o dia <b>" + dataVencimentoEmString + "</b> foi gerado" +
                        Constantes.CLOSE_PARAGRAPH +
                        "<p style='padding: 0 1rem; font-weight: 700; color: #19191d'>" +
                        "Clique no botão abaixo para visualizar sua cobrança" +
                        Constantes.CLOSE_PARAGRAPH +
                        "<div style='padding: 0 1rem'>" +
                        "<button style='padding: 0.5rem 1rem; background-color: #4a47d1; border: 1px solid #4a47d1; border-radius: 3px'>" +
                        "<a style='font-size: 0.9rem; color: #FFF;text-decoration: none;' href='" + pagamento.getLinkCobranca() + "'>" +
                        "Visualizar Cobrança" +
                        "</a>" +
                        "</button>" +
                        Constantes.CLOSE_DIV +
                        "<p style='padding: 0 1rem; color: #19191d'>" +
                        "Ou acesse " + pagamento.getLinkCobranca() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.LINE_BREAK +
                        "<p style='padding: 0 1rem; color: #19191d'>" +
                        "Atenciosamente," +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        "Subsy - gestão de assinaturas e cobranças recorrentes" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        "(11) 97981-5415" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>" +
                        "<p style='width: 100%; display: flex; justify-content: center; font-size: 0.7rem; color: #272930; font-weight: 600;'>" +
                        "Este e-mail está sendo enviado pela Subsy - Receber dos seus clientes nunca foi tão fácil" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        "</body>" +
                        "</html>";

        log.debug("HTML do e-mail criado com sucesso. Retornando valor...");
        return htmlMsg;
    }

}
