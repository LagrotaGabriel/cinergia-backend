package br.com.backend.modules.transferencia.hook.service.categorias.sucesso.utils;

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
public class SucessoTransferenciaWebHookUtil {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    EmailService emailService;

    @Async
    public void realizaCriacaoDeNotificacaoParaTransferenciaComSucesso(TransferenciaEntity transferenciaCriada) {
        //TODO VERIFICAR SE VAI APARECER PARA O USUÁRIO QUE A TRANSFERÊNCIA FOI REALIZADA MESMO AGENDADA PARA UM DIA DEPOIS. ISSO PODERIA DAR CONFLITO COM OS CLIENTES
        log.info("Método responsável pela implementação da lógica de criação de nova transferência acessado");
        NotificacaoEntity notificacaoCriada = new NotificacaoEntity().criaNovaNotificacaoEntity(
                transferenciaCriada.getEmpresa(),
                geraDescricaoParaCriacaoDeNotificacaoDeTransferenciaComSucesso(transferenciaCriada),
                null,
                TipoNotificacaoPlanoEnum.TRANSFERENCIA_SUCESSO);

        log.info("Realizando a persistência da notificação criada...");
        notificacaoRepository.save(notificacaoCriada);
        log.info("Notificação persistida com sucesso");
    }

    public String geraDescricaoParaCriacaoDeNotificacaoDeTransferenciaComSucesso(TransferenciaEntity transferenciaEntity) {
        return "Transferência de "
                + ConversorDeDados.converteValorDoubleParaValorMonetario(transferenciaEntity.getValor())
                + " para a chave pix "
                + transferenciaEntity.getChavePix()
                + " realizada com sucesso";
    }


    @Async
    public void realizaAcionamentoDoServicoDeEnvioDeEmails(TransferenciaEntity transferenciaPersistida) {
        //TODO CRIAR ENVIO DE E-MAIL PARA ENVIO DE TRANSFERÊNCIAS
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
