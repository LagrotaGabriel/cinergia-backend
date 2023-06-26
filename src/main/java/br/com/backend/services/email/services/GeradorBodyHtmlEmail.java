package br.com.backend.services.email.services;

import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class GeradorBodyHtmlEmail {

    @Value("${EMAIL_SUBSY_AWS}")
    String emailSubsyAws;

    public String geraBodyHtmlParaCobrancaAssinatura(PagamentoEntity pagamento,
                                                     PlanoEntity planoEntity,
                                                     ClienteEntity clienteEntity) {

        log.debug(Constantes.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug(Constantes.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));

        log.debug(Constantes.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                Constantes.OPEN_HTML +
                        Constantes.OPEN_HEAD +
                        Constantes.OPEN_TITLE +
                        "Sua fatura" +
                        Constantes.CLOSE_TITLE +
                        Constantes.CLOSE_HEAD +
                        Constantes.OPEN_BODY +
                        Constantes.OPEN_DIV_FULL_WIDTH +
                        Constantes.COLORED_TITLE_DIV_CONTAINER +
                        Constantes.COLORED_TITLE_H3 +
                        "Cobrança gerada" +
                        Constantes.CLOSE_H3 +
                        Constantes.CLOSE_DIV +
                        Constantes.TITLE_H3 +
                        Constantes.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        Constantes.CLOSE_H3 +
                        Constantes.DEFAULT_PARAGRAPH +
                        "O link de pagamento da sua assinatura <b>" + planoEntity.getDescricao().toUpperCase() + "</b> no valor de <b>" + valorCobrancaEmReais + "</b> com " +
                        "vencimento para o dia <b>" + dataVencimentoEmString + "</b> foi gerado" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar sua cobrança" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BUTTON_CONTAINER +
                        Constantes.OPEN_BUTTON +
                        Constantes.OPEN_ANCHOR + pagamento.getLinkCobranca() + "'>" +
                        "Visualizar Cobrança" +
                        Constantes.CLOSE_ANCHOR +
                        Constantes.CLOSE_BUTTON +
                        Constantes.CLOSE_DIV +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.OU_ACESSE + pagamento.getLinkCobranca() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.LINE_BREAK +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.ATENCIOSAMENTE +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.TELEFONE_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        Constantes.DIV_FOOTER +
                        Constantes.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        Constantes.CLOSE_BODY +
                        Constantes.CLOSE_HTML;

        log.debug(Constantes.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

    public String geraBodyHtmlParaAtrasoAssinatura(PagamentoEntity pagamento,
                                                   PlanoEntity planoEntity,
                                                   ClienteEntity clienteEntity) {

        log.debug(Constantes.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug("Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...");
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));

        log.debug(Constantes.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                Constantes.OPEN_HTML +
                        Constantes.OPEN_HEAD +
                        Constantes.OPEN_TITLE +
                        "Pagamento não localizado" +
                        Constantes.CLOSE_TITLE +
                        Constantes.CLOSE_HEAD +
                        Constantes.OPEN_BODY +
                        Constantes.OPEN_DIV_FULL_WIDTH +
                        Constantes.COLORED_TITLE_DIV_CONTAINER +
                        Constantes.COLORED_TITLE_H3 +
                        "Não registramos o pagamento da sua cobrança" +
                        Constantes.CLOSE_H3 +
                        Constantes.CLOSE_DIV +
                        Constantes.TITLE_H3 +
                        Constantes.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        Constantes.CLOSE_H3 +
                        Constantes.DEFAULT_PARAGRAPH +
                        "Não foi possível registrar o pagamento da sua cobrança <b>" + planoEntity.getDescricao().toUpperCase() +
                        "</b> no valor de <b>" + valorCobrancaEmReais + "</b> com vencimento para o dia <b>" + dataVencimentoEmString + "</b>" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar sua cobrança" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BUTTON_CONTAINER +
                        Constantes.OPEN_BUTTON +
                        Constantes.OPEN_ANCHOR + pagamento.getLinkCobranca() + "'>" +
                        "Visualizar Cobrança" +
                        Constantes.CLOSE_ANCHOR +
                        Constantes.CLOSE_BUTTON +
                        Constantes.CLOSE_DIV +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.OU_ACESSE + pagamento.getLinkCobranca() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.LINE_BREAK +
                        "Caso você tenha efetuado o pagamento desta cobrança nas últimas 48 horas, favor desconsiderar esta mensagem." +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.LINE_BREAK +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.ATENCIOSAMENTE +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.TELEFONE_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>" +
                        Constantes.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        Constantes.CLOSE_BODY +
                        Constantes.CLOSE_HTML;

        log.debug(Constantes.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

    public String geraBodyHtmlParaSucessoPagamento(PagamentoEntity pagamento,
                                                   PlanoEntity planoEntity,
                                                   ClienteEntity clienteEntity,
                                                   EmpresaEntity empresaEntity) {

        log.debug(Constantes.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug("Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...");
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));
        String dataPagamentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataPagamento()));

        log.debug(Constantes.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                Constantes.OPEN_HTML +
                        Constantes.OPEN_HEAD +
                        Constantes.OPEN_TITLE +
                        "Pagamento confirmado" +
                        Constantes.CLOSE_TITLE +
                        Constantes.CLOSE_HEAD +
                        Constantes.OPEN_BODY +
                        Constantes.OPEN_DIV_FULL_WIDTH +
                        Constantes.COLORED_TITLE_DIV_CONTAINER +
                        Constantes.COLORED_TITLE_H3 +
                        "Seu pagamento foi confirmado" +
                        Constantes.CLOSE_H3 +
                        Constantes.CLOSE_DIV +
                        Constantes.TITLE_H3 +
                        Constantes.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        Constantes.CLOSE_H3 +
                        Constantes.DEFAULT_PARAGRAPH +
                        "A sua cobrança foi <b>confirmada com sucesso</b>" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        "<b> Forma de pagamento: </b>" + pagamento.getFormaPagamento().getDesc() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        "<b> Valor pago: </b>" + valorCobrancaEmReais +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        "<b> Data do vencimento: </b>" + dataVencimentoEmString +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        "<b> Data do pagamento: </b>" + dataPagamentoEmString +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        "<b> Descrição da assinatura: </b>" + planoEntity.getDescricao() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar seu comprovante" +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.BUTTON_CONTAINER +
                        Constantes.OPEN_BUTTON +
                        Constantes.OPEN_ANCHOR + pagamento.getLinkComprovante() + "'>" +
                        "Visualizar comprovante" +
                        Constantes.CLOSE_ANCHOR +
                        Constantes.CLOSE_BUTTON +
                        Constantes.CLOSE_DIV +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.OU_ACESSE + pagamento.getLinkComprovante() +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.LINE_BREAK +
                        Constantes.DEFAULT_PARAGRAPH +
                        Constantes.ATENCIOSAMENTE +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.DEFAULT_LOWER_CASE_PARAGRAPH +
                        Constantes.TELEFONE_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>" +
                        "<p style='width: 100%; display: flex; justify-content: center; font-size: 0.7rem; color: #272930; font-weight: 600;'>" +
                        "Este documento e cobrança não possuem valor fiscal e são de responsabilidade única e exclusiva de " + empresaEntity.getNomeEmpresa() +
                        Constantes.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        Constantes.CLOSE_PARAGRAPH +
                        Constantes.CLOSE_DIV +
                        Constantes.CLOSE_BODY +
                        Constantes.CLOSE_HTML;

        log.debug(Constantes.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

}
