package br.com.backend.modules.transferencia.hook.service.categorias.falha.services;

import br.com.backend.modules.transferencia.hook.models.TransferWebHook;
import br.com.backend.modules.transferencia.hook.service.categorias.falha.utils.FalhaTransferenciaWebHookUtil;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.modules.transferencia.repository.impl.TransferenciaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FalhaTransferenciaWebHookService {

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    TransferenciaRepositoryImpl transferenciaRepositoryImpl;

    @Autowired
    FalhaTransferenciaWebHookUtil falhaTransferenciaWebHookUtil;

    public void realizaAtualizacaoDeStatusDeTransferenciaParaFalha(TransferWebHook eventoTransferencia) {

        log.info("Método responsável pelo tratamento de webhooks com transferências realizadas com erro acessado");

        log.info("Iniciando acesso ao método de implementação de busca de transferência por ID ASAAS...");
        TransferenciaEntity transferenciaEncontrada = transferenciaRepositoryImpl
                .implementaBuscaPorCodigoTransferenciaAsaas(eventoTransferencia.getId());
        log.info("Transferência encontrada com sucesso");

        log.info("Setando status da transferência como recusado...");
        transferenciaEncontrada.setStatusTransferencia(StatusTransferenciaEnum.FALHA);

        log.info("Iniciando persistência da transferência atualizada...");
        TransferenciaEntity transferenciaPersistida = transferenciaRepository.save(transferenciaEncontrada);
        log.info("Atualização do status da transferência para cancelado realizada com sucesso");

        log.info("Iniciando acesso ao método assincrono responsável por implementar a lógica de criação de " +
                "notificação para criação de novo pagamento...");
        falhaTransferenciaWebHookUtil
                .realizaCriacaoDeNotificacaoParaTransferenciaComFalha(transferenciaPersistida);
    }

}
