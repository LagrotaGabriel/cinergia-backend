package br.com.backend.modules.email.utils;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
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

        log.debug(ConstantesEmail.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug(ConstantesEmail.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));

        log.debug(ConstantesEmail.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                ConstantesEmail.OPEN_HTML +
                        ConstantesEmail.OPEN_HEAD +
                        ConstantesEmail.OPEN_TITLE +
                        "Sua fatura" +
                        ConstantesEmail.CLOSE_TITLE +
                        ConstantesEmail.CLOSE_HEAD +
                        ConstantesEmail.OPEN_BODY +
                        ConstantesEmail.OPEN_DIV_FULL_WIDTH +
                        ConstantesEmail.COLORED_TITLE_DIV_CONTAINER +
                        ConstantesEmail.COLORED_TITLE_H3 +
                        "Cobrança gerada" +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.TITLE_H3 +
                        ConstantesEmail.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "O link de pagamento da sua assinatura <b>" + planoEntity.getDescricao().toUpperCase() + "</b> no valor de <b>" + valorCobrancaEmReais + "</b> com " +
                        "vencimento para o dia <b>" + dataVencimentoEmString + "</b> foi gerado" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar sua cobrança" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BUTTON_CONTAINER +
                        ConstantesEmail.OPEN_BUTTON +
                        ConstantesEmail.OPEN_ANCHOR + pagamento.getLinkCobranca() + "'>" +
                        "Visualizar Cobrança" +
                        ConstantesEmail.CLOSE_ANCHOR +
                        ConstantesEmail.CLOSE_BUTTON +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.OU_ACESSE + pagamento.getLinkCobranca() +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.LINE_BREAK +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.ATENCIOSAMENTE +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.TELEFONE_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.DIV_FOOTER +
                        ConstantesEmail.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.CLOSE_BODY +
                        ConstantesEmail.CLOSE_HTML;

        log.debug(ConstantesEmail.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

    public String geraBodyHtmlParaAtrasoAssinatura(PagamentoEntity pagamento,
                                                   PlanoEntity planoEntity,
                                                   ClienteEntity clienteEntity) {

        log.debug(ConstantesEmail.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug("Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...");
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));

        log.debug(ConstantesEmail.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                ConstantesEmail.OPEN_HTML +
                        ConstantesEmail.OPEN_HEAD +
                        ConstantesEmail.OPEN_TITLE +
                        "Pagamento não localizado" +
                        ConstantesEmail.CLOSE_TITLE +
                        ConstantesEmail.CLOSE_HEAD +
                        ConstantesEmail.OPEN_BODY +
                        ConstantesEmail.OPEN_DIV_FULL_WIDTH +
                        ConstantesEmail.COLORED_TITLE_DIV_CONTAINER +
                        ConstantesEmail.COLORED_TITLE_H3 +
                        "Não registramos o pagamento da sua cobrança" +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.TITLE_H3 +
                        ConstantesEmail.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "Não foi possível registrar o pagamento da sua cobrança <b>" + planoEntity.getDescricao().toUpperCase() +
                        "</b> no valor de <b>" + valorCobrancaEmReais + "</b> com vencimento para o dia <b>" + dataVencimentoEmString + "</b>" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar sua cobrança" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BUTTON_CONTAINER +
                        ConstantesEmail.OPEN_BUTTON +
                        ConstantesEmail.OPEN_ANCHOR + pagamento.getLinkCobranca() + "'>" +
                        "Visualizar Cobrança" +
                        ConstantesEmail.CLOSE_ANCHOR +
                        ConstantesEmail.CLOSE_BUTTON +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.OU_ACESSE + pagamento.getLinkCobranca() +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.LINE_BREAK +
                        "Caso você tenha efetuado o pagamento desta cobrança nas últimas 48 horas, favor desconsiderar esta mensagem." +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.LINE_BREAK +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.ATENCIOSAMENTE +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.TELEFONE_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>" +
                        ConstantesEmail.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.CLOSE_BODY +
                        ConstantesEmail.CLOSE_HTML;

        log.debug(ConstantesEmail.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

    public String geraBodyHtmlParaSucessoPagamento(PagamentoEntity pagamento,
                                                   PlanoEntity planoEntity,
                                                   ClienteEntity clienteEntity,
                                                   EmpresaEntity empresaEntity) {

        log.debug(ConstantesEmail.METODO_GERAR_HTML_EMAIL_ACESSADO);

        log.debug("Iniciando criação das variáveis que serão utilizadas no corpo do e-mail...");
        String valorCobrancaEmReais = ConversorDeDados.converteValorDoubleParaValorMonetario(pagamento.getValorBruto());
        String dataVencimentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataVencimento()));
        String dataPagamentoEmString = (ConversorDeDados.converteDataUsParaDataBr(pagamento.getDataPagamento()));

        log.debug(ConstantesEmail.INICIANDO_CRIACAO_VARIAVEIS_CORPO_EMAIL);
        String htmlMsg =
                ConstantesEmail.OPEN_HTML +
                        ConstantesEmail.OPEN_HEAD +
                        ConstantesEmail.OPEN_TITLE +
                        "Pagamento confirmado" +
                        ConstantesEmail.CLOSE_TITLE +
                        ConstantesEmail.CLOSE_HEAD +
                        ConstantesEmail.OPEN_BODY +
                        ConstantesEmail.OPEN_DIV_FULL_WIDTH +
                        ConstantesEmail.COLORED_TITLE_DIV_CONTAINER +
                        ConstantesEmail.COLORED_TITLE_H3 +
                        "Seu pagamento foi confirmado" +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.TITLE_H3 +
                        ConstantesEmail.TITLE_H3_OLA + StringUtils.capitalize(clienteEntity.getNome()) +
                        ConstantesEmail.CLOSE_H3 +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "A sua cobrança foi <b>confirmada com sucesso</b>" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "<b> Forma de pagamento: </b>" + pagamento.getFormaPagamento().getDesc() +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "<b> Valor pago: </b>" + valorCobrancaEmReais +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "<b> Data do vencimento: </b>" + dataVencimentoEmString +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "<b> Data do pagamento: </b>" + dataPagamentoEmString +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        "<b> Descrição da assinatura: </b>" + planoEntity.getDescricao() +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BOLD_PARAGRAPH +
                        "Clique no botão abaixo para visualizar seu comprovante" +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.BUTTON_CONTAINER +
                        ConstantesEmail.OPEN_BUTTON +
                        ConstantesEmail.OPEN_ANCHOR + pagamento.getLinkComprovante() + "'>" +
                        "Visualizar comprovante" +
                        ConstantesEmail.CLOSE_ANCHOR +
                        ConstantesEmail.CLOSE_BUTTON +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.OU_ACESSE + pagamento.getLinkComprovante() +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.LINE_BREAK +
                        ConstantesEmail.DEFAULT_PARAGRAPH +
                        ConstantesEmail.ATENCIOSAMENTE +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.SUBSY_GESTAO_ASSINATURAS_RECORRENTES +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        emailSubsyAws +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.DEFAULT_LOWER_CASE_PARAGRAPH +
                        ConstantesEmail.TELEFONE_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        "<div style='width: 100%; padding: 0rem 1rem;background-color: #E7E7E7'>" +
                        "<p style='width: 100%; display: flex; justify-content: center; font-size: 0.7rem; color: #272930; font-weight: 600;'>" +
                        "Este documento e cobrança não possuem valor fiscal e são de responsabilidade única e exclusiva de " + empresaEntity.getNomeEmpresa() +
                        ConstantesEmail.PARAGRAPH_EMAIL_ENVIADO_POR_SUBSY +
                        ConstantesEmail.CLOSE_PARAGRAPH +
                        ConstantesEmail.CLOSE_DIV +
                        ConstantesEmail.CLOSE_BODY +
                        ConstantesEmail.CLOSE_HTML;

        log.debug(ConstantesEmail.HTML_EMAIL_CRIADO);
        return htmlMsg;
    }

}
