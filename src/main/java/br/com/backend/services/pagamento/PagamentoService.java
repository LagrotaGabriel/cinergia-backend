package br.com.backend.services.pagamento;

import br.com.backend.models.dto.pagamento.response.PagamentoPageResponse;
import br.com.backend.models.entities.*;
import br.com.backend.models.enums.*;
import br.com.backend.proxy.webhooks.cobranca.AtualizacaoCobrancaWebHook;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import br.com.backend.repositories.pagamento.PagamentoRepository;
import br.com.backend.repositories.pagamento.impl.PagamentoRepositoryImpl;
import br.com.backend.repositories.plano.impl.PlanoRepositoryImpl;
import br.com.backend.services.email.services.EmailService;
import br.com.backend.services.empresa.EmpresaService;
import br.com.backend.services.plano.PlanoService;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    @Autowired
    PagamentoTypeConverter pagamentoTypeConverter;

    @Autowired
    PlanoService planoService;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    EmailService emailService;

    @Value("${TAXA_SISTEMA_PERCENTUAL}")
    Double taxaSistemaPercentual;
    @Value("${TAXA_SISTEMA_FIXA}")
    Double taxaSistemaFixa;
    @Value("${TAXA_ASAAS_PERCENTUAL_BOLETO}")
    Double taxaAsaasBoletoPercentual;
    @Value("${TAXA_ASAAS_FIXA_BOLETO}")
    Double taxaAsaasBoletoFixa;
    @Value("${TAXA_ASAAS_PERCENTUAL_PIX}")
    Double taxaAsaasPixPercentual;
    @Value("${TAXA_ASAAS_FIXA_PIX}")
    Double taxaAsaasPixFixa;
    @Value("${TAXA_ASAAS_PERCENTUAL_CARTAO_CREDITO}")
    Double taxaAsaasCartaoCreditoPercentual;
    @Value("${TAXA_ASAAS_FIXA_CARTAO_CREDITO}")
    Double taxaAsaasCartaoCreditoFixa;

    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoCliente(EmpresaEntity empresaLogada,
                                                                            Pageable pageable,
                                                                            Long idCliente) {
        log.debug("Método de serviço de obtenção paginada de pagamentos do cliente acessado.");

        log.debug("Acessando repositório de busca de pagamentos do cliente");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository.buscaPorPagamentosDoCliente(pageable, empresaLogada.getId(), idCliente);

        log.debug(Constantes.CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE);
        PagamentoPageResponse pagamentoPageResponse = pagamentoTypeConverter.converteListaDePagamentosEntityParaPagamentosResponse(pagamentoPage);
        log.debug(Constantes.CONVERSAO_TIPAGEM_SUCESSO);

        log.info(Constantes.BUSCA_PAGINADA_PAGAMENTOS_SUCESSO);
        return pagamentoPageResponse;
    }

    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoPlano(EmpresaEntity empresaLogada,
                                                                          Pageable pageable,
                                                                          Long idPlano) {
        log.debug("Método de serviço de obtenção paginada de pagamentos acessado.");

        log.debug("Acessando repositório de busca de pagamentos");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository.buscaPorPagamentosDoPlano(pageable, empresaLogada.getId(), idPlano);

        log.debug(Constantes.CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE);
        PagamentoPageResponse pagamentoPageResponse = pagamentoTypeConverter.converteListaDePagamentosEntityParaPagamentosResponse(pagamentoPage);
        log.debug(Constantes.CONVERSAO_TIPAGEM_SUCESSO);

        log.info(Constantes.BUSCA_PAGINADA_PAGAMENTOS_SUCESSO);
        return pagamentoPageResponse;
    }

    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentos(EmpresaEntity empresaLogada,
                                                                   Pageable pageable,
                                                                   String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de pagamentos acessado.");

        log.debug("Acessando repositório de busca de clientes");
        Page<PagamentoEntity> pagamentoPage = campoBusca != null && !campoBusca.isEmpty()
                ? pagamentoRepository.buscaPorPagamentosTypeAhead(pageable, (campoBusca).toUpperCase(), empresaLogada.getId())
                : pagamentoRepository.buscaPorPagamentos(pageable, empresaLogada.getId());

        log.debug(Constantes.CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE);
        PagamentoPageResponse pagamentoPageResponse = pagamentoTypeConverter.converteListaDePagamentosEntityParaPagamentosResponse(pagamentoPage);
        log.debug(Constantes.CONVERSAO_TIPAGEM_SUCESSO);

        log.info(Constantes.BUSCA_PAGINADA_PAGAMENTOS_SUCESSO);
        return pagamentoPageResponse;
    }

    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosRealizados(EmpresaEntity empresaLogada,
                                                                             Pageable pageable) {
        log.debug("Método de serviço de obtenção paginada de pagamentos realizados acessado.");

        log.debug("Acessando repositório de busca de clientes");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository.buscaPorPagamentosRealizados(pageable, empresaLogada.getId());

        log.debug(Constantes.CONVERTE_PAGAMENTO_DE_ENTITY_PARA_RESPONSE);
        PagamentoPageResponse pagamentoPageResponse = pagamentoTypeConverter.converteListaDePagamentosEntityParaPagamentosResponse(pagamentoPage);
        log.debug(Constantes.CONVERSAO_TIPAGEM_SUCESSO);

        log.info(Constantes.BUSCA_PAGINADA_PAGAMENTOS_SUCESSO);
        return pagamentoPageResponse;
    }

    public Double calculaValorTaxaPagamento(PagamentoEntity pagamento) {

        log.debug("Método de cálculo de valor líquido do pagamento acessado");

        log.debug("Iniciando setagem das variáveis do método...");
        Double valorBrutoPagamento = pagamento.getValorBruto();
        double taxaTotal = 0.0;

        log.debug("Rodando estrutura condicional para direcionar taxa para sua forma de pagamento correspondente...");
        switch (pagamento.getFormaPagamento()) {
            case PIX: {
                taxaTotal += (((valorBrutoPagamento / 100) * taxaAsaasPixPercentual) + taxaAsaasPixFixa);
                break;
            }
            case BOLETO: {
                taxaTotal += (((valorBrutoPagamento / 100) * taxaAsaasBoletoPercentual) + taxaAsaasBoletoFixa);
                break;
            }
            case CREDIT_CARD: {
                taxaTotal += (((valorBrutoPagamento / 100) * taxaAsaasCartaoCreditoPercentual) + taxaAsaasCartaoCreditoFixa);
                break;
            }
            default: {
                taxaTotal += (((valorBrutoPagamento / 100) * taxaAsaasPixPercentual) + taxaAsaasPixFixa);
            }
        }
        log.debug("Taxa da integradora calculada: {}", taxaTotal);

        taxaTotal += (((valorBrutoPagamento / 100) * taxaSistemaPercentual) + taxaSistemaFixa);
        log.debug("Taxa total calculada: {}", taxaTotal);

        log.debug("Método executado com sucesso. Retornando valor líquido: {}...", valorBrutoPagamento - taxaTotal);
        return taxaTotal;
    }

    @Transactional
    public void realizaTratamentoWebhookCobranca(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook) {

        log.debug("Método 'motor de distruição' de Webhook de atualização de cobrança acessado");

        PlanoEntity plano = planoRepositoryImpl
                .implementaBuscaPorCodigoPlanoAsaas(atualizacaoCobrancaWebHook.getPayment().getSubscription());

        EmpresaEntity empresa =
                empresaRepositoryImpl.implementaBuscaPorId(plano.getIdEmpresaResponsavel());

        switch (atualizacaoCobrancaWebHook.getEvent()) {
            case PAYMENT_CREATED:
                log.debug("Condicional para novo pagamento CRIADO acessada");
                realizaCriacaoDeNovoPagamento(atualizacaoCobrancaWebHook, plano, empresa);
                log.info("Cobrança CRIADA com sucesso");
                break;
            case PAYMENT_RECEIVED:
            case PAYMENT_CONFIRMED:
                log.debug("Condicional de pagamento CONFIRMADO acessada");
                realizaAtualizacaoDePagamentoRealizado(atualizacaoCobrancaWebHook, plano);
                log.info("Cobrança CONFIRMADA sucesso");
                break;
            case PAYMENT_UPDATED:
                log.debug("Condicional de pagamento ALTERADO acessada");
                realizaAtualizacaoDePagamentoAlterado(atualizacaoCobrancaWebHook, plano);
                log.info("Cobrança ALTERADA com sucesso");
                break;
            case PAYMENT_OVERDUE:
                log.debug("Condicional de pagamento VENCIDO acessada");
                realizaAtualizacaoDePlanoParaPagamentoVencido(atualizacaoCobrancaWebHook, plano);
                log.info("Atualização de plano para pagamento VENCIDO realizada com sucesso");
                break;
            case PAYMENT_DELETED:
            case PAYMENT_ANTICIPATED:
            case PAYMENT_AWAITING_RISK_ANALYSIS:
            case PAYMENT_APPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_REPROVED_BY_RISK_ANALYSIS:
            case PAYMENT_RESTORED:
            case PAYMENT_REFUNDED:
            case PAYMENT_RECEIVED_IN_CASH_UNDONE:
            case PAYMENT_CHARGEBACK_REQUESTED:
            case PAYMENT_CHARGEBACK_DISPUTE:
            case PAYMENT_AWAITING_CHARGEBACK_REVERSAL:
            case PAYMENT_DUNNING_RECEIVED:
            case PAYMENT_DUNNING_REQUESTED:
            case PAYMENT_BANK_SLIP_VIEWED:
            case PAYMENT_CHECKOUT_VIEWED:
                break;
        }
    }

    public void realizaCriacaoDeNovoPagamento(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                              PlanoEntity planoEntity,
                                              EmpresaEntity empresa) {

        log.debug("Método de criação de novo pagamento acessado");

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorId(planoEntity.getIdClienteResponsavel(),
                planoEntity.getIdEmpresaResponsavel());

        log.debug("Iniciando construção do objeto PagamentoEntity com valores recebidos pelo ASAAS...");
        PagamentoEntity pagamentoEntity = PagamentoEntity.builder()
                .idEmpresaResponsavel(planoEntity.getIdEmpresaResponsavel())
                .idPlanoResponsavel(planoEntity.getId())
                .idAsaas(atualizacaoCobrancaWebHook.getPayment().getId())
                .idClienteResponsavel(planoEntity.getIdClienteResponsavel())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalDate.now().toString())
                .dataPagamento(null)
                .horaPagamento(null)
                .valorBruto(atualizacaoCobrancaWebHook.getPayment().getValue())
                .valorLiquidoAsaas(atualizacaoCobrancaWebHook.getPayment().getNetValue())
                .descricao(atualizacaoCobrancaWebHook.getPayment().getDescription())
                .dataVencimento(atualizacaoCobrancaWebHook.getPayment().getDueDate())
                .linkBoletoAsaas(atualizacaoCobrancaWebHook.getPayment().getBankSlipUrl())
                .linkCobranca(atualizacaoCobrancaWebHook.getPayment().getInvoiceUrl())
                .linkComprovante(atualizacaoCobrancaWebHook.getPayment().getTransactionReceiptUrl())
                .formaPagamento(FormaPagamentoEnum.valueOf(atualizacaoCobrancaWebHook
                        .getPayment().getBillingType().getFormaPagamentoResumida()))
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build();
        log.debug(Constantes.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.debug("Adicionando objeto pagamento criado à lista de pagamentos do plano...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug("Iniciando construção do objeto notificação para transferência realizada com sucesso");
        NotificacaoEntity notificacaoEntity = NotificacaoEntity.builder()
                .idEmpresaResponsavel(empresa.getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .descricao(planoEntity.getDescricao().toUpperCase() + " - Cobrança criada no valor de "
                        + ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentoEntity.getValorBruto()))
                .uri(atualizacaoCobrancaWebHook.getPayment().getInvoiceUrl())
                .tipoNotificacaoEnum(TipoNotificacaoEnum.COBRANCA_CRIADA)
                .lida(false)
                .build();

        log.debug("Adicionando notificação ao objeto empresa...");
        empresa.getNotificacoes().add(notificacaoEntity);
        empresaRepositoryImpl.implementaPersistencia(empresa);

        try {
            log.debug("Iniciando acesso ao método de implementação de persistência do plano para o persistir com sua lista " +
                    "de pagamentos atualizada...");
            planoRepositoryImpl.implementaPersistencia(planoEntity);

            if (planoEntity.getNotificacoes().contains(NotificacaoEnum.EMAIL) && cliente.getEmail() != null) {
                log.debug(Constantes.INICIA_SERVICO_ENVIO_EMAILS);
                emailService.enviarEmailCobranca(pagamentoEntity, planoEntity, cliente);
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.warn("Recebido webhook duplicado para criação de cobrança: {}. Nenhuma persistência foi realizada",
                    dataIntegrityViolationException.getMessage());
        }
    }

    public void realizaAtualizacaoDePagamentoRealizado(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                       PlanoEntity planoEntity) {
        log.debug(Constantes.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(atualizacaoCobrancaWebHook.getPayment().getId());

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorId(planoEntity.getIdClienteResponsavel(),
                planoEntity.getIdEmpresaResponsavel());

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        EmpresaEntity empresa = empresaRepositoryImpl.implementaBuscaPorId(planoEntity.getIdEmpresaResponsavel());

        log.debug(Constantes.REMOVENDO_PAGAMENTO_DO_PLANO, pagamentoEntity);
        planoEntity.getPagamentos().remove(pagamentoEntity);

        log.debug("Iniciando setagem da data de vencimento do plano do cliente para o próximo vencimento...");
        planoEntity.setDataVencimento(planoService
                .consultaAssinaturaAsaas(atualizacaoCobrancaWebHook.getPayment().getSubscription()).getNextDueDate());

        log.debug("Setando plano do cliente como ATIVO...");
        planoEntity.setStatusPlano(StatusPlanoEnum.ATIVO);

        log.debug("Atualizando variáveis do objeto pagamento...");
        pagamentoEntity.setIdPlanoResponsavel(planoEntity.getId());
        pagamentoEntity.setIdEmpresaResponsavel(planoEntity.getIdEmpresaResponsavel());
        pagamentoEntity.setIdAsaas(atualizacaoCobrancaWebHook.getPayment().getId());
        pagamentoEntity.setIdClienteResponsavel(planoEntity.getIdClienteResponsavel());
        pagamentoEntity.setIdPlanoResponsavel(planoEntity.getId());
        pagamentoEntity.setDataPagamento(LocalDate.now().toString());
        pagamentoEntity.setHoraPagamento(LocalTime.now().toString());
        pagamentoEntity.setValorBruto(atualizacaoCobrancaWebHook.getPayment().getValue());
        pagamentoEntity.setTaxaTotal(calculaValorTaxaPagamento(pagamentoEntity));
        pagamentoEntity.setValorLiquidoAsaas(atualizacaoCobrancaWebHook.getPayment().getNetValue());
        pagamentoEntity.setLinkBoletoAsaas(atualizacaoCobrancaWebHook.getPayment().getBankSlipUrl());
        pagamentoEntity.setLinkCobranca(atualizacaoCobrancaWebHook.getPayment().getInvoiceUrl());
        pagamentoEntity.setLinkComprovante(atualizacaoCobrancaWebHook.getPayment().getTransactionReceiptUrl());
        pagamentoEntity.setFormaPagamento(FormaPagamentoEnum.valueOf(atualizacaoCobrancaWebHook
                .getPayment().getBillingType().getFormaPagamentoResumida()));
        pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.APROVADO);
        log.debug(Constantes.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.debug("Adicionando objeto pagamento criado à lista de pagamentos do plano...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug("Iniciando construção do objeto notificação para transferência realizada com sucesso");
        NotificacaoEntity notificacaoEntity = NotificacaoEntity.builder()
                .idEmpresaResponsavel(empresa.getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .descricao("Pagamento de "
                        + ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentoEntity.getValorBruto())
                        + " da assinatura " + planoEntity.getDescricao().toUpperCase() + " recebido com sucesso")
                .uri(atualizacaoCobrancaWebHook.getPayment().getTransactionReceiptUrl())
                .tipoNotificacaoEnum(TipoNotificacaoEnum.COBRANCA_RECEBIDA)
                .lida(false)
                .build();

        log.debug("Adicionando notificação ao objeto empresa...");
        empresa.getNotificacoes().add(notificacaoEntity);

        log.debug(Constantes.INICIANDO_IMPL_PERSISTENCIA_PLANO);
        planoRepositoryImpl.implementaPersistencia(planoEntity);

        log.debug("Iniciando acesso ao método de atualização do saldo da empresa...");
        empresaService.adicionaSaldoContaEmpresa(empresa, pagamentoEntity);

        if (planoEntity.getNotificacoes().contains(NotificacaoEnum.EMAIL) && cliente.getEmail() != null) {
            log.debug(Constantes.INICIA_SERVICO_ENVIO_EMAILS);
            emailService.enviarEmailSucessoPagamento(pagamentoEntity, planoEntity, cliente, empresa);
        }
    }

    public void realizaAtualizacaoDePlanoParaPagamentoVencido(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                              PlanoEntity planoEntity) {

        log.debug("Método de atualização de pagamento e plano como vencidos acessado");

        log.debug(Constantes.BUSCA_CLIENTE_POR_ID);
        ClienteEntity cliente = clienteRepositoryImpl.implementaBuscaPorId(planoEntity.getIdClienteResponsavel(),
                planoEntity.getIdEmpresaResponsavel());

        log.debug(Constantes.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(atualizacaoCobrancaWebHook.getPayment().getId());

        log.debug(Constantes.REMOVENDO_PAGAMENTO_DO_PLANO, pagamentoEntity);
        planoEntity.getPagamentos().remove(pagamentoEntity);

        log.debug("Setando status do pagamento como atrasado...");
        pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.ATRASADO);

        log.debug("Adicionado pagamento atualizado à lista de pagamentos do plano...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug("Setando plano do cliente como INATIVO...");
        planoEntity.setStatusPlano(StatusPlanoEnum.INATIVO);

        log.debug(Constantes.INICIANDO_IMPL_PERSISTENCIA_PLANO);
        planoRepositoryImpl.implementaPersistencia(planoEntity);

        if (planoEntity.getNotificacoes().contains(NotificacaoEnum.EMAIL) && cliente.getEmail() != null) {
            log.debug(Constantes.INICIA_SERVICO_ENVIO_EMAILS);
            emailService.enviarEmailAtrasoPagamento(pagamentoEntity, planoEntity, cliente);
        }
    }

    public void realizaAtualizacaoDePagamentoAlterado(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                      PlanoEntity planoEntity) {
        log.debug(Constantes.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(atualizacaoCobrancaWebHook.getPayment().getId());

        log.debug(Constantes.REMOVENDO_PAGAMENTO_DO_PLANO, pagamentoEntity);
        planoEntity.getPagamentos().remove(pagamentoEntity);

        log.debug("Atualizando variáveis do objeto pagamento...");
        pagamentoEntity.setDescricao(atualizacaoCobrancaWebHook.getPayment().getDescription());
        pagamentoEntity.setFormaPagamento(FormaPagamentoEnum.valueOf(atualizacaoCobrancaWebHook
                .getPayment().getBillingType().getFormaPagamentoResumida()));

        log.debug(Constantes.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.debug("Setando pagamento à lista de pagamentos do plano...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug(Constantes.INICIANDO_IMPL_PERSISTENCIA_PLANO);
        planoRepositoryImpl.implementaPersistencia(planoEntity);
    }

}
