package br.com.backend.services.pagamento;

import br.com.backend.models.dto.pagamento.response.PagamentoPageResponse;
import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.enums.FormaPagamentoEnum;
import br.com.backend.models.enums.StatusPagamentoEnum;
import br.com.backend.models.enums.StatusPlanoEnum;
import br.com.backend.proxy.webhooks.cobranca.AtualizacaoCobrancaWebHook;
import br.com.backend.repositories.pagamento.PagamentoRepository;
import br.com.backend.repositories.pagamento.impl.PagamentoRepositoryImpl;
import br.com.backend.repositories.plano.impl.PlanoRepositoryImpl;
import br.com.backend.services.plano.PlanoService;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    PagamentoTypeConverter pagamentoTypeConverter;

    @Autowired
    PlanoService planoService;

    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoPlano(EmpresaEntity empresaLogada,
                                                                          Pageable pageable,
                                                                          Long idPlano) {
        log.debug("Método de serviço de obtenção paginada de pagamentos acessado.");

        log.debug("Acessando repositório de busca de pagamentos");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository.buscaPorPagamentosDoPlano(pageable, empresaLogada.getId(), idPlano);

        log.debug("Busca de pagamentos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PagamentoPageResponse pagamentoPageResponse = pagamentoTypeConverter.converteListaDePagamentosEntityParaPagamentosResponse(pagamentoPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return pagamentoPageResponse;
    }

    public void realizaTratamentoWebhookCobranca(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook) {

        log.debug("Método 'motor de distruição' de Webhook de atualização de cobrança acessado");

        PlanoEntity plano = planoRepositoryImpl
                .implementaBuscaPorCodigoPlanoAsaas(atualizacaoCobrancaWebHook.getPayment().getSubscription());

        switch (atualizacaoCobrancaWebHook.getEvent()) {
            case PAYMENT_CREATED:
                log.debug("Condicional para novo pagamento CRIADO acessada");
                realizaCriacaoDeNovoPagamento(atualizacaoCobrancaWebHook, plano);
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
            case PAYMENT_REFUNDED: //TODO Implantar lógica para estorno
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
                                              PlanoEntity planoEntity) {
        log.debug("Iniciando construção do objeto PagamentoEntity com valores recebidos pelo ASAAS...");
        PagamentoEntity pagamentoEntity = PagamentoEntity.builder()
                .idEmpresaResponsavel(planoEntity.getIdEmpresaResponsavel())
                .idPlanoResponsavel(planoEntity.getId())
                .idAsaas(atualizacaoCobrancaWebHook.getPayment().getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalDate.now().toString())
                .dataPagamento(null)
                .horaPagamento(null)
                .valorBruto(atualizacaoCobrancaWebHook.getPayment().getValue())
                .valorLiquidoAsaas(atualizacaoCobrancaWebHook.getPayment().getNetValue())
                .descricao(atualizacaoCobrancaWebHook.getPayment().getDescription())
                .dataVencimento(atualizacaoCobrancaWebHook.getPayment().getDueDate())
                .formaPagamento(FormaPagamentoEnum.valueOf(atualizacaoCobrancaWebHook
                        .getPayment().getBillingType().getFormaPagamentoResumida()))
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build();
        log.debug(Constantes.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.debug("Adicionando objeto pagamento criado à lista de pagamentos do plano...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug("Iniciando acesso ao método de implementação de persistência do plano para o persistir com sua lista " +
                "de pagamentos atualizada...");
        planoRepositoryImpl.implementaPersistencia(planoEntity);
    }

    public void realizaAtualizacaoDePagamentoRealizado(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                       PlanoEntity planoEntity) {
        log.debug(Constantes.INICIANDO_IMPLEMENTACAO_BUSCA_PAGAMENTO_ASAAS);
        PagamentoEntity pagamentoEntity = pagamentoRepositoryImpl
                .implementaBuscaPorCodigoPagamentoAsaas(atualizacaoCobrancaWebHook.getPayment().getId());

        log.debug(Constantes.REMOVENDO_PAGAMENTO_DO_PLANO, pagamentoEntity);
        planoEntity.getPagamentos().remove(pagamentoEntity);

        log.debug("Iniciando setagem da data de vencimento do plano do cliente para o próximo vencimento...");
        planoEntity.setDataVencimento(planoService
                .consultaAssinaturaAsaas(atualizacaoCobrancaWebHook.getPayment().getSubscription()).getNextDueDate());

        log.debug("Setando plano do cliente como ATIVO...");
        planoEntity.setStatusPlano(StatusPlanoEnum.ATIVO);

        log.debug("Atualizando variáveis do objeto pagamento...");
        pagamentoEntity.setIdPlanoResponsavel(planoEntity.getId());
        pagamentoEntity.setDataPagamento(LocalDate.now().toString());
        pagamentoEntity.setHoraPagamento(LocalTime.now().toString());
        pagamentoEntity.setValorBruto(atualizacaoCobrancaWebHook.getPayment().getValue());
        pagamentoEntity.setValorLiquidoAsaas(atualizacaoCobrancaWebHook.getPayment().getNetValue());
        pagamentoEntity.setFormaPagamento(FormaPagamentoEnum.valueOf(atualizacaoCobrancaWebHook
                .getPayment().getBillingType().getFormaPagamentoResumida()));
        pagamentoEntity.setStatusPagamento(StatusPagamentoEnum.APROVADO);
        log.debug(Constantes.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.debug("Setando pagamento à lista de pagamentos do cliente...");
        planoEntity.getPagamentos().add(pagamentoEntity);

        log.debug(Constantes.INICIANDO_IMPL_PERSISTENCIA_PLANO);
        planoRepositoryImpl.implementaPersistencia(planoEntity);
    }

    public void realizaAtualizacaoDePlanoParaPagamentoVencido(AtualizacaoCobrancaWebHook atualizacaoCobrancaWebHook,
                                                              PlanoEntity planoEntity) {

        log.debug("Método de atualização de pagamento e plano como vencidos acessado");

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
