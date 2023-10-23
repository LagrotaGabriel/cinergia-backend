package br.com.backend.modules.transferencia.hook.service.categorias.sucesso.services;

import br.com.backend.exceptions.custom.InternalErrorException;
import br.com.backend.modules.transferencia.hook.models.TransferWebHook;
import br.com.backend.modules.transferencia.hook.service.categorias.sucesso.utils.SucessoTransferenciaWebHookUtil;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.modules.transferencia.repository.impl.TransferenciaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SucessoTransferenciaWebHookService {

    @Autowired
    SucessoTransferenciaWebHookUtil sucessoTransferenciaWebHookUtil;

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    TransferenciaRepositoryImpl transferenciaRepositoryImpl;

    public void tratamentoWebhookTransferenciaComSucesso(TransferWebHook eventoTransferencia) {
        log.info("Método responsável pelo tratamento de webhooks com transferências realizadas com sucesso acessado");

        log.info("Iniciando acesso ao método de implementação de busca de transferência por ID ASAAS...");
        TransferenciaEntity transferenciaEncontrada = transferenciaRepositoryImpl
                .implementaBuscaPorCodigoTransferenciaAsaas(eventoTransferencia.getId());
        log.info("Transferência encontrada com sucesso");

        //TODO VERIFICAR SE COM QUERY UPDATE PARA ATUALIZAR STATUS FICA MELHOR (NOS QUESITOS DESEMPENHO E QUALIDADE DE CÓDIGO)
        log.info("Setando status da transferência como aprovado...");
        transferenciaEncontrada.setStatusTransferencia(StatusTransferenciaEnum.SUCESSO);

        try {
            log.info("Iniciando persistência da transferência atualizada...");
            TransferenciaEntity transferenciaPersistida = transferenciaRepository.save(transferenciaEncontrada);
            log.info("Persistência da transferência com status atualizado realizada com sucesso");

            log.info("Iniciando acesso ao método assincrono responsável por implementar a lógica de criação de " +
                    "notificação para criação de novo pagamento...");
            sucessoTransferenciaWebHookUtil
                    .realizaCriacaoDeNotificacaoParaTransferenciaComSucesso(transferenciaPersistida);

            log.info("Iniciando acesso ao método assincrono utilitário de validação de direcionamento para " +
                    "o serviço de e-mails...");
            sucessoTransferenciaWebHookUtil
                    .realizaAcionamentoDoServicoDeEnvioDeEmails(transferenciaPersistida);

            log.info("Método Webhook de atualização de status de transferência para SUCESSO finalizado com sucesso");
        } catch (Exception e) {
            log.error("Ocorreu um erro durante a atualização do status da transferência: {}", e.getMessage());
            throw new InternalErrorException("Ocorreu um erro durante a atualização do status da transferência: "
                    + e.getMessage());
        }

    }

}
