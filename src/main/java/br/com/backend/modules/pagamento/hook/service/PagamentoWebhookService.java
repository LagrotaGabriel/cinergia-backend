package br.com.backend.modules.pagamento.hook.service;

import br.com.backend.modules.pagamento.hook.models.AtualizacaoPagamentoWebHook;
import br.com.backend.modules.pagamento.hook.service.categorias.atualizado.AtualizacaoPagamentoWebhookService;
import br.com.backend.modules.pagamento.hook.service.categorias.confirmado.services.ConfirmacaoPagamentoWebhookService;
import br.com.backend.modules.pagamento.hook.service.categorias.criado.services.CriacaoPagamentoWebhookService;
import br.com.backend.modules.pagamento.hook.service.categorias.removido.RemocaoPagamentoWebhookService;
import br.com.backend.modules.pagamento.hook.service.categorias.vencido.services.AtrasoPagamentoWebhookService;
import br.com.backend.modules.pagamento.models.responses.PagamentoResponse;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class PagamentoWebhookService {

    @Autowired
    CriacaoPagamentoWebhookService criacaoPagamentoWebhookService;

    @Autowired
    ConfirmacaoPagamentoWebhookService confirmacaoPagamentoWebhookService;

    @Autowired
    AtualizacaoPagamentoWebhookService atualizacaoPagamentoWebhookService;

    @Autowired
    AtrasoPagamentoWebhookService atrasoPagamentoWebhookService;

    @Autowired
    RemocaoPagamentoWebhookService remocaoPagamentoWebhookService;

    @Transactional
    public void realizaRedirecionamentoParaMetodoCorreto(AtualizacaoPagamentoWebHook atualizacaoPagamentoWebHook) {
        log.info("Método orquestrador de Webhook de pagamentos acessado");

        log.info("Iniciando orquestração por tipo de atualização de pagamento do webhook...");
        switch (atualizacaoPagamentoWebHook.getEvent()) {
            case PAYMENT_CREATED:
                log.info("Condicional para novo pagamento CRIADO acessada");
                criacaoPagamentoWebhookService.realizaCriacaoDeNovoPagamento(
                        atualizacaoPagamentoWebHook.getPayment());
                log.info("Cobrança CRIADA com sucesso");
                break;
            case PAYMENT_RECEIVED:
            case PAYMENT_CONFIRMED:
                log.info("Condicional de pagamento CONFIRMADO acessada");
                confirmacaoPagamentoWebhookService.realizaAtualizacaoDePagamentoRealizado(
                        atualizacaoPagamentoWebHook.getPayment());
                log.info("Cobrança CONFIRMADA sucesso");
                break;
            case PAYMENT_UPDATED:
                log.info("Condicional de pagamento ALTERADO acessada");
                atualizacaoPagamentoWebhookService.realizaAtualizacaoDePagamentoAlterado(
                        atualizacaoPagamentoWebHook.getPayment());
                log.info("Cobrança ALTERADA com sucesso");
                break;
            case PAYMENT_OVERDUE:
                log.info("Condicional de pagamento VENCIDO acessada");
                atrasoPagamentoWebhookService.realizaAtualizacaoDePlanoParaPagamentoVencido(
                        atualizacaoPagamentoWebHook.getPayment());
                log.info("Atualização de plano para pagamento VENCIDO realizada com sucesso");
                break;
            case PAYMENT_DELETED:
                log.info("Condicional de pagamento DELETADO acessada");
                remocaoPagamentoWebhookService.realizaAtualizacaoDePlanoParaPagamentoRemovido(
                        atualizacaoPagamentoWebHook.getPayment());
                log.info("Atualização de plano para pagamento DELETADO realizada com sucesso");
                break;
            case PAYMENT_ANTICIPATED:
            case PAYMENT_AWAITING_RISK_ANALYSIS:
            case PAYMENT_APPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_REPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_RESTORED:
            case PAYMENT_REFUNDED:
            case PAYMENT_RECEIVED_IN_CASH_UNDONE:
            case PAYMENT_CHARGEBACK_REQUESTED:
            case PAYMENT_CHARGEBACK_DISPUTE:
            case PAYMENT_AWAITING_CHARGEBACK_REVERSAL:
            case PAYMENT_DUNNING_RECEIVED:
            case PAYMENT_DUNNING_REQUESTED:
            case PAYMENT_BANK_SLIP_VIEWED:
            case PAYMENT_CHECKOUT_VIEWED:
                break;
        }
    }

}
