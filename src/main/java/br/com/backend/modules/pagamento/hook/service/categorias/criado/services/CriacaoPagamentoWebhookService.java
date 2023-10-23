package br.com.backend.modules.pagamento.hook.service.categorias.criado.services;

import br.com.backend.exceptions.custom.InternalErrorException;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.hook.service.categorias.criado.utils.CriacaoPagamentoWebhookUtil;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.repository.impl.PlanoRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CriacaoPagamentoWebhookService {

    @Autowired
    CriacaoPagamentoWebhookUtil criacaoPagamentoWebhookUtil;

    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Transactional
    public void realizaCriacaoDeNovoPagamento(PagamentoWebHookRequest pagamentoWebHookRequest) {

        log.info("Método de criação de novo pagamento acessado");

        log.info("Iniciando obtenção do plano responsável pelo pagamento...");
        PlanoEntity planoPai = planoRepositoryImpl
                .implementaBuscaPorCodigoPlanoAsaas(pagamentoWebHookRequest.getSubscription());
        log.info("Plano obtido com sucesso");

        log.info("Setando data de vencimento no plano pai...");
        planoPai.setDataVencimento(pagamentoWebHookRequest.getDueDate());
        log.info("Data de vencimento setada com sucesso no plano pai");

        log.info("Obtendo empresa...");
        EmpresaEntity empresaResponsavel = planoPai.getEmpresa();
        log.info("Empresa obtida com sucesso");

        log.info("Iniciando acesso ao método responsável pela conversão de objeto do tipo " +
                "PagamentoWebHookRequest para objeto do tipo PagamentoEntity...");
        PagamentoEntity pagamentoEntity = new PagamentoEntity()
                .constroiPagamentoEntityParaCriacao(empresaResponsavel, planoPai, pagamentoWebHookRequest);
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.info("Acoplando pagamento ao plano pai...");
        planoPai.addPagamento(pagamentoEntity);
        log.info("Plano acoplado ao pagamento com sucesso");

        try {
            log.info("Iniciando persistência de pagamento acomplado no plano pai...");
            PagamentoEntity pagamentoPersistido =
                    planoRepositoryImpl.implementaPersistencia(planoPai).obtemUltimoPagamentoPersistido();
            log.info("Persistência do plano pai com pagamento acoplado realizada com sucesso");

            log.info("Iniciando acesso ao método assincrono responsável por implementar a lógica de criação de " +
                    "notificação para criação de novo pagamento...");
            criacaoPagamentoWebhookUtil.realizaCriacaoDeNotificacaoParaCriacaoDePagamento(pagamentoPersistido);

            log.info("Iniciando acesso ao método assincrono utilitário de validação de direcionamento para o serviço de e-mails...");
            criacaoPagamentoWebhookUtil.realizaAcionamentoDoServicoDeEnvioDeEmails(pagamentoPersistido);

            log.info("Método Webhook de criação de novo pagamento finalizado com sucesso");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.warn("Recebido webhook duplicado para criação de cobrança: {}. Nenhuma persistência foi realizada",
                    dataIntegrityViolationException.getMessage());
        } catch (Exception e) {
            log.error("Ocorreu um erro durante a criação de um novo pagamento: {}", e.getMessage());
            throw new InternalErrorException("Ocorreu um erro durante a criação de um novo pagamento: " + e.getMessage());
        }
    }

}
