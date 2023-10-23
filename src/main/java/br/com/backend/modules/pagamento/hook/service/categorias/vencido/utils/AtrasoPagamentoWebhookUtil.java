package br.com.backend.modules.pagamento.hook.service.categorias.vencido.utils;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.email.services.EmailService;
import br.com.backend.modules.email.utils.ConstantesEmail;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AtrasoPagamentoWebhookUtil {

    @Autowired
    EmailService emailService;

    @Async
    public void realizaAcionamentoDoServicoDeEnvioDeEmails(PagamentoEntity pagamentoAtualizado) {
        log.info("Método utilitário de validação de acionamento do serviço de e-mails acessado");

        log.info("Obtendo plano do pagamento...");
        PlanoEntity planoPaiPagamento = pagamentoAtualizado.getPlano();
        log.info("Plano obtido com sucesso");

        log.info("Obtendo cliente do plano...");
        ClienteEntity clientePaiPlano = planoPaiPagamento.getCliente();
        log.info("Cliente obtido com sucesso");

        log.info("Validando se plano referenciado está autorizado a enviar notificações por e-mails e se o cliente " +
                "pai do plano possui algum e-mail cadastrado...");
        if (planoPaiPagamento.getNotificacoes().contains(NotificacaoEnum.EMAIL) && clientePaiPlano.getEmail() != null) {
            log.info(ConstantesEmail.INICIA_SERVICO_ENVIO_EMAILS);
            emailService.enviarEmailSucessoPagamento(
                    pagamentoAtualizado,
                    planoPaiPagamento,
                    clientePaiPlano,
                    pagamentoAtualizado.getEmpresa());
        } else log.warn("O serviço de e-mails não pode ser acionado para o plano referenciado");
    }

    public void realizaAtualizacaoNoObjetoPagamentoParaAtraso(PagamentoEntity pagamentoEntity) {
        log.info("Setando status do pagamento como atrasado...");
        pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.ATRASADO);

        log.info("Setando plano do pagamento como INATIVO...");
        pagamentoEntity.getPlano().setStatusPlano(StatusPlanoEnum.INATIVO);
    }

}
