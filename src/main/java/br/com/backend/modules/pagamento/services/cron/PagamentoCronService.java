package br.com.backend.modules.pagamento.services.cron;

import org.springframework.transaction.annotation.Transactional;

public interface PagamentoCronService {
    @Transactional
    void verificaPagamentosAtrasadosComStatusAtivo();
}
