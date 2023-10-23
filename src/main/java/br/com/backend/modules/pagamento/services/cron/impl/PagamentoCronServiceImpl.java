package br.com.backend.modules.pagamento.services.cron.impl;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.pagamento.services.cron.PagamentoCronService;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
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
public class PagamentoCronServiceImpl implements PagamentoCronService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Override
    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    public void verificaPagamentosAtrasadosComStatusAtivo() {

        //TODO MÉTODO AINDA NÃO FOI TESTADO. TESTAR QUANDO LOCALXPOSE VOLTAR. PONTOS DE ATENÇÃO: PERSISTÊNCIA BIDIRECIONAL E QUERY DE BUSCAS

        log.info("[CHECK DIÁRIO] Verificando pagamentos atrasados que possuam status diferente de atrasado...");

        log.info("Obtendo data atual...");
        String hoje = String.valueOf(LocalDate.now());
        log.info("Data atual obtida com sucesso: {}", hoje);

        log.info("Iniciando obtenção de pagamentos atrasados com status diferente de atrasado e cancelado...");
        List<PagamentoEntity> pagamentos = pagamentoRepository
                .buscaPagamentosAtrasadosComStatusDiferenteDeAtrasadoECancelado(hoje);
        log.info("Busca no banco de dados realizada por sucesso");

        if (pagamentos.isEmpty()) {
            log.info("Finalizando método: Nenhum pagamento atrasado com status diferente de atrasado e cancelado foi encontrado");
            return;
        }

        log.info("Iniciando iteração pelos pagamentos atrasados...");
        pagamentos.forEach(pagamento -> {
            log.info("Iteração pelo pagamento de id {} iniciada", pagamento.getUuid());

            log.info("Setando status do pagamento como removido...");
            pagamento.setStatusPagamento(StatusPagamentoEnum.ATRASADO);

            log.info("Setando status do plano do pagamento como inativo...");
            pagamento.getPlano().setStatusPlano(StatusPlanoEnum.INATIVO);
            log.info("Status do plano setado como inativo com sucesso");
        });
        log.info("Iteração de pagamentos finalizada");

        log.info("Iniciando persistência no banco de dados com pagamentos atualizados...");
        pagamentoRepository.saveAll(pagamentos);
        log.info("Persistência finalizada com sucesso");

        log.info("Método finalizado com sucesso");
    }
}