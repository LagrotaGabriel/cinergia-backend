package br.com.backend.modules.pagamento.hook.service.categorias.criado.utils;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.email.services.EmailService;
import br.com.backend.modules.email.utils.ConstantesEmail;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import br.com.backend.modules.notificacao.repository.NotificacaoRepository;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CriacaoPagamentoWebhookUtil {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    EmailService emailService;

    @Async
    public void realizaCriacaoDeNotificacaoParaCriacaoDePagamento(PagamentoEntity pagamentoCriado) {
        log.info("Método responsável pela implementação da lógica de criação de novo pagamento acessado");
        NotificacaoEntity notificacaoCriada = new NotificacaoEntity().criaNovaNotificacaoEntity(
                pagamentoCriado.getEmpresa(),
                geraDescricaoParaCriacaoDeNotificacaoDeCriacaoDePagamento(pagamentoCriado),
                pagamentoCriado.getLinkCobranca(),
                TipoNotificacaoPlanoEnum.COBRANCA_CRIADA);

        log.info("Realizando a persistência da notificação criada...");
        notificacaoRepository.save(notificacaoCriada);
        log.info("Notificação persistida com sucesso");
    }

    public String geraDescricaoParaCriacaoDeNotificacaoDeCriacaoDePagamento(PagamentoEntity pagamentoCriado) {
        return pagamentoCriado.getPlano().getDescricao().toUpperCase()
                + " - Cobrança criada no valor de "
                + ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentoCriado.getValorBruto());
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
            emailService.enviarEmailCobranca(pagamentoPersistido, planoPaiPagamento, clientePaiPlano);
        } else log.warn("O serviço de e-mails não pode ser acionado para o plano referenciado");
    }

}
