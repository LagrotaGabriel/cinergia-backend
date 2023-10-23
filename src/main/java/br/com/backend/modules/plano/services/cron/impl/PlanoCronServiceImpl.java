package br.com.backend.modules.plano.services.cron.impl;

import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import br.com.backend.modules.plano.proxy.operations.remocao.impl.RemocaoPlanoAsaasProxyImpl;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.plano.services.cron.PlanoCronService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduled
 * Observação técnica sobre o @Scheduled
 * 1: segundo (preenchido de 0 a 59)
 * 2: minuto (preenchido de 0 a 59)
 * 3: hora (preenchido de 0 a 23)
 * 4: dia (preenchido de 0 a 31)
 * 5: mês (preenchido de 1 a 12)
 */
@Slf4j
@Service
public class PlanoCronServiceImpl implements PlanoCronService {

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    RemocaoPlanoAsaasProxyImpl remocaoPlanoAsaasProxyImpl;

    @Override
    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    public void verificaPlanosComCancelamentoAgendadoParaHoje() {
        log.info("[CHECK DIÁRIO] Verificando planos que possuem cancelamento pendente...");

        log.info("Obtendo data atual...");
        String hoje = String.valueOf(LocalDate.now());
        log.info("Data atual obtida com sucesso: {}", hoje);

        log.info("Iniciando obtenção de planos com data de agendamento de remoção pendentes...");
        List<PlanoEntity> planos = planoRepository.buscaPlanosComAgendamentosDeRemocaoPendentes(hoje);

        if (planos.isEmpty()) {
            log.info("Finalizando método: Nenhum plano foi encontrado com agendamento de remoção para hoje");
            return;
        }

        log.info("Iniciando iteração pelos planos agendados...");
        planos.forEach(plano -> {
            log.info("Iteração pelo plano de id {} iniciada", plano.getUuid());

            log.info("Setando status do plano como removido...");
            plano.setStatusPlano(StatusPlanoEnum.REMOVIDO);

            log.info("Iniciando acesso ao método de cancelamento de plano na integradora Asaas...");
            remocaoPlanoAsaasProxyImpl.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(plano.getAsaasId());
        });
        log.info("Iteração de planos finalizada");

        log.info("Iniciando persistência no banco de dados com planos atualizados...");
        planoRepository.saveAll(planos);
        log.info("Persistência finalizada com sucesso");

        log.info("Método finalizado com sucesso");
    }
}