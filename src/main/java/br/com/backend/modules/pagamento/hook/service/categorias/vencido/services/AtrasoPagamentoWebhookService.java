package br.com.backend.modules.pagamento.hook.service.categorias.vencido.services;

import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.hook.service.categorias.vencido.utils.AtrasoPagamentoWebhookUtil;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.repository.impl.PagamentoRepositoryImpl;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AtrasoPagamentoWebhookService {

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    @Autowired
    AtrasoPagamentoWebhookUtil atrasoPagamentoWebhookUtil;

    @Transactional
    public void realizaAtualizacaoDePlanoParaPagamentoVencido(PagamentoWebHookRequest pagamentoWebHookRequest) {

        log.info("Método de atualização de pagamento e plano como vencidos acessado");

        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info("Iniciando acesso ao método responsável pela setagem dos dados de atualização do pagamento...");
        atrasoPagamentoWebhookUtil.realizaAtualizacaoNoObjetoPagamentoParaAtraso(pagamentoEntity);

        log.info("Iniciando persistência do pagamento atualizado...");
        PagamentoEntity pagamentoAtualizado = pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);

        log.info("Iniciando acesso ao método assíncrono de envio de e-mails...");
        atrasoPagamentoWebhookUtil.realizaAcionamentoDoServicoDeEnvioDeEmails(pagamentoAtualizado);

        log.info("Lógica de atualização de status do pagamento para ATRASADO finalizada com sucesso");
    }
}
