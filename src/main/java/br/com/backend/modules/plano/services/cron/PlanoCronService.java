package br.com.backend.modules.plano.services.cron;


import org.springframework.transaction.annotation.Transactional;

public interface PlanoCronService {

    @Transactional
    void verificaPlanosComCancelamentoAgendadoParaHoje();

}
