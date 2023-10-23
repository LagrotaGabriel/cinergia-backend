package br.com.backend.modules.pagamento.hook.service.categorias.confirmado.services;

import br.com.backend.modules.empresa.services.EmpresaService;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.hook.service.categorias.confirmado.utils.ConfirmacaoPagamentoWebhookUtil;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.repository.impl.PagamentoRepositoryImpl;
import br.com.backend.modules.pagamento.services.PagamentoService;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ConfirmacaoPagamentoWebhookService {

    @Autowired
    ConfirmacaoPagamentoWebhookUtil confirmacaoPagamentoWebhookUtil;

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    EmpresaService empresaService;

    @Transactional
    public void realizaAtualizacaoDePagamentoRealizado(PagamentoWebHookRequest pagamentoWebHookRequest) {
        log.info("Método de atualização de status de pagamento para pagamento realizado acessado");

        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEncontrado = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info("Iniciando acesso ao método responsável por realizar a atualização dos dados do plano do pagamento...");
        pagamentoEncontrado.setPlano(
                confirmacaoPagamentoWebhookUtil.realizaAtualizacaoDoPlano(
                        pagamentoEncontrado.getPlano(),
                        pagamentoWebHookRequest));

        log.info(ConstantesPagamento.ATUALIZANDO_VARIAVEIS_PAGAMENTO);
        PagamentoEntity pagamentoEntity = new PagamentoEntity()
                .constroiObjetoPagamentoParaPagamentoConfirmado(
                        pagamentoEncontrado,
                        pagamentoWebHookRequest);

        log.info("Iniciando acesso ao método de setagem de taxa sob o pagamento realizado...");
        pagamentoEntity.setTaxaTotal(pagamentoService.calculaValorTaxaPagamento(pagamentoEntity));

        try {
            log.info("Iniciando acesso ao método de implementação da persistência do pagamento atualizado...");
            PagamentoEntity pagamentoPersistido = pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);

            log.info("Iniciando acesso ao método assíncrono responsável por implementar a lógica de criação de " +
                    "notificação para atualização de pagamento confirmado...");
            confirmacaoPagamentoWebhookUtil.realizaCriacaoDeNotificacaoParaPagamentoConfirmado(pagamentoPersistido);

            log.info("Iniciando acesso ao método assíncrono de atualização do saldo da empresa...");
            empresaService.adicionaSaldoContaEmpresa(pagamentoPersistido);

            log.info("Iniciando acesso ao método assíncrono utilitário de validação de direcionamento para o serviço " +
                    "de e-mails...");
            confirmacaoPagamentoWebhookUtil.realizaAcionamentoDoServicoDeEnvioDeEmails(pagamentoPersistido);

            log.info("Método Webhook de atualização de status de pagamento CONFIRMADO finalizado com sucesso");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.warn("Recebido webhook duplicado para atualização de status de cobrança para confirmado: {}. Nenhuma " +
                    "persistência foi realizada", dataIntegrityViolationException.getMessage());
        }
    }

}
