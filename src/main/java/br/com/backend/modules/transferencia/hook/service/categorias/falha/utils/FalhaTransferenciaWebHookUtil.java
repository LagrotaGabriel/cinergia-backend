package br.com.backend.modules.transferencia.hook.service.categorias.falha.utils;

import br.com.backend.modules.email.services.EmailService;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import br.com.backend.modules.notificacao.repository.NotificacaoRepository;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FalhaTransferenciaWebHookUtil {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    EmailService emailService;

    @Async
    public void realizaCriacaoDeNotificacaoParaTransferenciaComFalha(TransferenciaEntity transferencia) {
        log.info("Método responsável pela implementação da lógica de criação de notificação de transferência " +
                "com falha acessado");
        NotificacaoEntity notificacaoCriada = new NotificacaoEntity().criaNovaNotificacaoEntity(
                transferencia.getEmpresa(),
                geraDescricaoParaCriacaoDeNotificacaoDeTransferenciaComSucesso(transferencia),
                null,
                TipoNotificacaoPlanoEnum.TRANSFERENCIA_ERRO);

        log.info("Realizando a persistência da notificação criada...");
        notificacaoRepository.save(notificacaoCriada);
        log.info("Notificação persistida com sucesso");
    }

    public String geraDescricaoParaCriacaoDeNotificacaoDeTransferenciaComSucesso(TransferenciaEntity transferenciaEntity) {
        return "Ocorreu um erro durante a transferência para a chave pix " + transferenciaEntity.getChavePix();
    }


    @Async
    public void realizaAcionamentoDoServicoDeEnvioDeEmails(TransferenciaEntity transferencia) {
        //TODO CRIAR ENVIO DE E-MAIL PARA TRANSFERÊNCIAS COM ERRO
        log.warn("MÉTODO DE ENVIO DE E-MAILS PARA ATUALIZAÇÃO DE STATUS DE TRANSFERÊNCIA " +
                "DESATIVADO. IMPLEMENTAR FUNCIONALIDADE");
//        log.info("Método utilitário de validação de acionamento do serviço de e-mails acessado");
//        log.info("Validando se plano referenciado está autorizado a enviar notificações por e-mails e se o cliente " +
//                "pai do plano possui algum e-mail cadastrado...");
//        if (planoPaiPagamento.getNotificacoes().contains(NotificacaoEnum.EMAIL) && clientePaiPlano.getEmail() != null) {
//            log.info(ConstantesEmail.INICIA_SERVICO_ENVIO_EMAILS);
//            emailService.enviarEmailCobranca(transferenciaPersistida, planoPaiPagamento, clientePaiPlano);
//        } else log.warn("O serviço de e-mails não pode ser acionado para o plano referenciado");
    }

}
