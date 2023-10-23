package br.com.backend.modules.pagamento.hook.service.categorias.confirmado.utils;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.email.services.EmailService;
import br.com.backend.modules.email.utils.ConstantesEmail;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import br.com.backend.modules.notificacao.repository.NotificacaoRepository;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import br.com.backend.modules.plano.proxy.operations.consulta.impl.ConsultaPlanoAsaasProxyImpl;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmacaoPagamentoWebhookUtil {

    @Autowired
    EmailService emailService;

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    ConsultaPlanoAsaasProxyImpl consultaPlanoAsaasProxyImpl;

    @Async
    public void realizaCriacaoDeNotificacaoParaPagamentoConfirmado(PagamentoEntity pagamentoConfirmado) {
        log.info("Método responsável pela implementação da lógica de criação de novo pagamento acessado");
        NotificacaoEntity notificacaoCriada = new NotificacaoEntity().criaNovaNotificacaoEntity(
                pagamentoConfirmado.getEmpresa(),
                geraDescricaoParaCriacaoDeNotificacaoDePagamentoConfirmado(pagamentoConfirmado),
                pagamentoConfirmado.getLinkComprovante(),
                TipoNotificacaoPlanoEnum.COBRANCA_RECEBIDA);

        log.info("Realizando a persistência da notificação criada...");
        notificacaoRepository.save(notificacaoCriada);
        log.info("Notificação persistida com sucesso");
    }

    public String geraDescricaoParaCriacaoDeNotificacaoDePagamentoConfirmado(PagamentoEntity pagamentoConfirmado) {
        return "Pagamento de "
                + ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentoConfirmado.getValorBruto())
                + " da assinatura "
                + pagamentoConfirmado.getPlano().getDescricao().toUpperCase()
                + " recebido com sucesso";
    }

    @Async
    public void realizaAcionamentoDoServicoDeEnvioDeEmails(PagamentoEntity pagamentoPersistido) {
        log.info("Método utilitário de validação de acionamento do serviço de e-mails acessado");

        log.info("Obtendo plano do pagamento...");
        PlanoEntity planoPaiPagamento = pagamentoPersistido.getPlano();
        log.info("Plano obtido com sucesso");

        log.info("Obtendo cliente do plano...");
        ClienteEntity clientePaiPlano = planoPaiPagamento.getCliente();
        log.info("Cliente obtido com sucesso");

        log.info("Validando se plano referenciado está autorizado a enviar notificações por e-mails e se o cliente " +
                "pai do plano possui algum e-mail cadastrado...");
        if (planoPaiPagamento.getNotificacoes().contains(NotificacaoEnum.EMAIL) && clientePaiPlano.getEmail() != null) {
            log.info(ConstantesEmail.INICIA_SERVICO_ENVIO_EMAILS);
            emailService.enviarEmailSucessoPagamento(
                    pagamentoPersistido,
                    planoPaiPagamento,
                    clientePaiPlano,
                    pagamentoPersistido.getEmpresa());
        } else log.warn("O serviço de e-mails não pode ser acionado para o plano referenciado");
    }

    public PlanoEntity realizaAtualizacaoDoPlano(PlanoEntity plano,
                                                 PagamentoWebHookRequest pagamentoWebHookRequest) {

        log.info("Iniciando setagem da data de vencimento do plano do pagamento para o próximo vencimento...");
        plano.setDataVencimento(consultaPlanoAsaasProxyImpl.consultaAssinaturaAsaas(
                pagamentoWebHookRequest.getSubscription()).getNextDueDate());

        log.info("Setando plano do pagamento como ATIVO...");
        plano.setStatusPlano(StatusPlanoEnum.ATIVO);

        return plano;
    }
}
