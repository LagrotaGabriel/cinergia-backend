package br.com.backend.modules.pagamento.hook.service.categorias.atualizado;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.repository.impl.PagamentoRepositoryImpl;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AtualizacaoPagamentoWebhookService {

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    public void realizaAtualizacaoDePagamentoAlterado(PagamentoWebHookRequest pagamentoWebHookRequest) {
        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info(ConstantesPagamento.ATUALIZANDO_VARIAVEIS_PAGAMENTO);
        pagamentoEntity.setDescricao(pagamentoWebHookRequest.getDescription());
        pagamentoEntity.setFormaPagamento(FormaPagamentoEnum.valueOf(
                pagamentoWebHookRequest.getBillingType().getFormaPagamentoResumida()));
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.info("Iniciando persistência do pagamento atualizado...");
        pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);
        log.info("Pagamento atualizado com sucesso");
    }
}
