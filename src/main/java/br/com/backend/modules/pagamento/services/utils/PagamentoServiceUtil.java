package br.com.backend.modules.pagamento.services.utils;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.responses.page.PagamentoPageResponse;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PagamentoServiceUtil {

    public PagamentoPageResponse realizaConversaoDePagamentosPage(Page<PagamentoEntity> pagamentoPage) {
        log.info(ConstantesPagamento.CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE);
        PagamentoPageResponse pagamentoPageResponse =
                new PagamentoPageResponse().constroiPagamentoPageResponse(pagamentoPage);
        log.info(Constantes.CONVERSAO_TIPAGEM_SUCESSO);

        log.info(ConstantesPagamento.BUSCA_PAGINADA_PAGAMENTOS_SUCESSO);
        return pagamentoPageResponse;
    }
}
