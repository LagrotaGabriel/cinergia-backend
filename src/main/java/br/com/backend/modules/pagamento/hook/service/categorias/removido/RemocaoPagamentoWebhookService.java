package br.com.backend.modules.pagamento.hook.service.categorias.removido;

import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.pagamento.repository.impl.PagamentoRepositoryImpl;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemocaoPagamentoWebhookService {

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    public void realizaAtualizacaoDePlanoParaPagamentoRemovido(PagamentoWebHookRequest pagamentoWebHookRequest) {
        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(pagamentoWebHookRequest.getId());

        log.info(ConstantesPagamento.ATUALIZANDO_VARIAVEIS_PAGAMENTO);
        pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.CANCELADO);
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.info(ConstantesPlano.INICIANDO_IMPL_PERSISTENCIA_PLANO);
        pagamentoRepositoryImpl.implementaPersistencia(pagamentoEntity);
        log.info("Atualização do status do pagamento para cancelado realizada com sucesso");
    }
}
