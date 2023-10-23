package br.com.backend.modules.transferencia.hook.service.categorias.cancelada;

import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.modules.transferencia.hook.models.TransferWebHook;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.modules.transferencia.repository.impl.TransferenciaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CancelamentoTransferenciaWebHookService {

    @Autowired
    TransferenciaRepositoryImpl transferenciaRepositoryImpl;

    @Autowired
    TransferenciaRepository transferenciaRepository;

    public void realizaAtualizacaoDeStatusDeTransferenciaParaCancelado(TransferWebHook eventoTransferencia) {
        log.info(ConstantesPagamento.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);

        log.info("Iniciando acesso ao método de implementação de busca de transferência por ID ASAAS...");
        TransferenciaEntity transferenciaEncontrada = transferenciaRepositoryImpl
                .implementaBuscaPorCodigoTransferenciaAsaas(eventoTransferencia.getId());
        log.info("Transferência encontrada com sucesso");

        log.info("Iniciando atualização do status da transferência para cancelado...");
        transferenciaEncontrada.setStatusTransferencia(StatusTransferenciaEnum.CANCELADO);

        log.info("Iniciando persistência da transferência atualizada...");
        transferenciaRepository.save(transferenciaEncontrada);
        log.info("Atualização do status da transferência para cancelado realizada com sucesso");
    }

}
