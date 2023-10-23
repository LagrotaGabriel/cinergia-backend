package br.com.backend.modules.transferencia.hook.service;

import br.com.backend.modules.transferencia.hook.models.AtualizacaoTransferenciaWebHook;
import br.com.backend.modules.transferencia.hook.service.categorias.cancelada.CancelamentoTransferenciaWebHookService;
import br.com.backend.modules.transferencia.hook.service.categorias.falha.services.FalhaTransferenciaWebHookService;
import br.com.backend.modules.transferencia.hook.service.categorias.sucesso.services.SucessoTransferenciaWebHookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransferenciaWebHookService {

    @Autowired
    SucessoTransferenciaWebHookService sucessoTransferenciaWebHookService;

    @Autowired
    CancelamentoTransferenciaWebHookService cancelamentoTransferenciaWebHookService;

    @Autowired
    FalhaTransferenciaWebHookService falhaTransferenciaWebHookService;

    @Transactional
    public void realizaRedirecionamentoParaMetodoCorreto(AtualizacaoTransferenciaWebHook atualizacaoTransferenciaWebHook) {
        log.info("Método orquestrador de Webhook de pagamentos acessado");

        //TODO REMOVER
        System.err.println(atualizacaoTransferenciaWebHook);

        log.info("Iniciando orquestração por tipo de atualização de transferência do webhook...");
        switch (atualizacaoTransferenciaWebHook.getEvent()) {
            case "RECEIVED" -> {
                log.info("Condicional para transferência realizada com sucesso acessada");
                sucessoTransferenciaWebHookService.tratamentoWebhookTransferenciaComSucesso(
                        atualizacaoTransferenciaWebHook.getTransfer());
                log.info("Transferência criada com sucesso");
            }
            case "" -> {
                // TODO VERIFICAR QUAL NOME DO EVENTO PARA CANCELADO
                log.info("Condicional de transferência cancelada acessada");
                cancelamentoTransferenciaWebHookService.realizaAtualizacaoDeStatusDeTransferenciaParaCancelado(
                        atualizacaoTransferenciaWebHook.getTransfer());
                log.info("Transferência cancelada com sucesso");
            }
            case "FAILURE" -> {
                // TODO VERIFICAR QUAL NOME DO EVENTO PARA FALHA
                log.info("Condicional de transferência com falha acessada");
                falhaTransferenciaWebHookService.realizaAtualizacaoDeStatusDeTransferenciaParaFalha(
                        atualizacaoTransferenciaWebHook.getTransfer());
                log.info("Transferência cancelada com sucesso");
            }
        }
    }

}
