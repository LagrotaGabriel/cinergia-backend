package br.com.backend.modules.email.services;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.email.utils.GeradorBodyHtmlEmail;
import br.com.backend.util.Constantes;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    GeradorBodyHtmlEmail geradorBodyHtmlEmail;

    @Value("${AWS_ACCESS_KEY}")
    String awsAccessKey;

    @Value("${AWS_SECRET_KEY}")
    String awsSecretKey;

    @Value("${EMAIL_SUBSY_AWS}")
    String emailSubsyAws;

    private AWSCredentialsProvider obtemCredenciaisAwsSes() {

        log.debug("Método responsável por obter as credenciais AWS acessado");

        log.debug("Realizando obtenção das credenciais AWS através do objeto awsCredentialsProvider...");
        AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {
            @Override
            public void refresh() {
            }

            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSSecretKey() {
                        return awsSecretKey;
                    }

                    @Override
                    public String getAWSAccessKeyId() {
                        return awsAccessKey;
                    }
                };
            }
        };
        log.debug("Credenciais AWS obtidas com sucesso. Retornando...");
        return awsCredentialsProvider;
    }

    private AmazonSimpleEmailService obtemClientAwsSes() {
        log.debug("Método de obtenção do cliente AWS SES Acessado");

        log.debug("Iniciando construção do objeto AmazonSimpleEmailService...");
        AmazonSimpleEmailService client =
                AmazonSimpleEmailServiceClientBuilder.standard()
                        .withCredentials(obtemCredenciaisAwsSes())
                        .withRegion(Regions.US_EAST_1)
                        .build();
        log.debug("Objeto amazonSimpleEmailService buildado com sucesso. Retornando...");
        return client;
    }

    public void enviarEmailCobranca(PagamentoEntity pagamento, PlanoEntity planoEntity, ClienteEntity clienteEntity) {
        log.debug("Método responsável por enviar e-mail de cobrança acessado");
        try {
            log.debug(Constantes.INICIANDO_ACESSO_OBTENCAO_CLIENT_AWS_SES);
            AmazonSimpleEmailService client = obtemClientAwsSes();

            log.debug(Constantes.INICIANDO_CONSTRUCAO_OBJETO_REQUEST_EMAIL);
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(clienteEntity.getEmail()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset(Constantes.UTF_8).withData(geradorBodyHtmlEmail.
                                                    geraBodyHtmlParaCobrancaAssinatura(pagamento, planoEntity, clienteEntity)))
                                    .withText(new Content()
                                            .withCharset(Constantes.UTF_8).withData("Fatura")))
                            .withSubject(new Content()
                                    .withCharset(Constantes.UTF_8).withData("A fatura da sua assinatura já está disponível - " + planoEntity.getDescricao())))
                    .withSource(emailSubsyAws);
            log.debug(Constantes.CORPO_EMAIL_CRIADO);

            log.debug(Constantes.INICIANDO_ENVIO_EMAIL);
            client.sendEmail(request);

            log.info("E-mail de cobrança enviado com sucesso");
        } catch (Exception ex) {
            log.error("Ocorreu um erro no envio do e-mail de cobrança: {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void enviarEmailAtrasoPagamento(PagamentoEntity pagamento, PlanoEntity planoEntity, ClienteEntity clienteEntity) {
        log.debug("Método responsável por enviar e-mail de atraso de pagamento acessado");
        try {
            log.debug(Constantes.INICIANDO_ACESSO_OBTENCAO_CLIENT_AWS_SES);
            AmazonSimpleEmailService client = obtemClientAwsSes();

            log.debug(Constantes.INICIANDO_CONSTRUCAO_OBJETO_REQUEST_EMAIL);
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(clienteEntity.getEmail()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset(Constantes.UTF_8).withData(geradorBodyHtmlEmail.
                                                    geraBodyHtmlParaAtrasoAssinatura(pagamento, planoEntity, clienteEntity)))
                                    .withText(new Content()
                                            .withCharset(Constantes.UTF_8).withData("Atraso")))
                            .withSubject(new Content()
                                    .withCharset(Constantes.UTF_8).withData("Não registramos o pagamento da sua assinatura - " + planoEntity.getDescricao())))
                    .withSource(emailSubsyAws);
            log.debug(Constantes.CORPO_EMAIL_CRIADO);

            log.debug(Constantes.INICIANDO_ENVIO_EMAIL);
            client.sendEmail(request);

            log.info("E-mail de atraso enviado com sucesso");
        } catch (Exception ex) {
            log.error("Ocorreu um erro no envio do e-mail de atraso: {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void enviarEmailSucessoPagamento(PagamentoEntity pagamento,
                                            PlanoEntity planoEntity,
                                            ClienteEntity clienteEntity,
                                            EmpresaEntity empresaEntity) {
        log.debug("Método responsável por enviar e-mail de pagamento confirmado acessado");
        try {
            log.debug(Constantes.INICIANDO_ACESSO_OBTENCAO_CLIENT_AWS_SES);
            AmazonSimpleEmailService client = obtemClientAwsSes();

            log.debug(Constantes.INICIANDO_CONSTRUCAO_OBJETO_REQUEST_EMAIL);
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(clienteEntity.getEmail()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset(Constantes.UTF_8).withData(geradorBodyHtmlEmail.
                                                    geraBodyHtmlParaSucessoPagamento(pagamento, planoEntity, clienteEntity, empresaEntity)))
                                    .withText(new Content()
                                            .withCharset(Constantes.UTF_8).withData("Atraso")))
                            .withSubject(new Content()
                                    .withCharset(Constantes.UTF_8).withData("Seu pagamento foi confirmado - " + planoEntity.getDescricao())))
                    .withSource(emailSubsyAws);
            log.debug(Constantes.CORPO_EMAIL_CRIADO);

            log.debug(Constantes.INICIANDO_ENVIO_EMAIL);
            client.sendEmail(request);

            log.info("E-mail de pagamento confirmado enviado com sucesso");
        } catch (Exception ex) {
            log.error("Ocorreu um erro no envio do e-mail de atraso: {}", ex.getMessage());
            ex.printStackTrace();
        }
    }

}