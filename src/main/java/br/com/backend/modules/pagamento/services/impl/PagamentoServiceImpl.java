package br.com.backend.modules.pagamento.services.impl;

import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.hook.models.PagamentoWebHookRequest;
import br.com.backend.modules.pagamento.hook.models.enums.BillingTypeEnum;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.entity.id.PagamentoId;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.pagamento.models.responses.PagamentoResponse;
import br.com.backend.modules.pagamento.models.responses.page.PagamentoPageResponse;
import br.com.backend.modules.pagamento.proxy.impl.PagamentoAsaasProxyImpl;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.pagamento.repository.impl.PagamentoRepositoryImpl;
import br.com.backend.modules.pagamento.services.PagamentoService;
import br.com.backend.modules.pagamento.services.utils.PagamentoServiceUtil;
import br.com.backend.modules.pagamento.services.validator.PagamentoValidator;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import br.com.backend.modules.plano.repository.impl.PlanoRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PagamentoServiceImpl implements PagamentoService {

    //TODO REMOVER VARIÁVEL PARA TESTE
    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Autowired
    PagamentoAsaasProxyImpl pagamentoAsaasProxyImpl;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PagamentoRepositoryImpl pagamentoRepositoryImpl;

    @Autowired
    PagamentoServiceUtil pagamentoServiceUtil;

    @Autowired
    PagamentoValidator pagamentoValidator;

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

    @Override
    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentos(UUID uuidEmpresaSessao,
                                                                   String campoBusca,
                                                                   Pageable pageable) {

        log.info("Método de serviço de obtenção paginada de pagamentos acessado.");

        log.info("Acessando repositório de busca de pagamentos");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository
                .buscaPaginadaPorPagamentos(pageable, uuidEmpresaSessao, campoBusca);

        return pagamentoServiceUtil.realizaConversaoDePagamentosPage(pagamentoPage);
    }

    @Override
    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosRealizados(UUID uuidEmpresaSessao,
                                                                             Pageable pageable) {
        log.info("Método de serviço de obtenção paginada de pagamentos realizados acessado.");

        log.info("Acessando repositório de busca de clientes");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository
                .buscaPorPagamentosRealizados(pageable, uuidEmpresaSessao);

        return pagamentoServiceUtil.realizaConversaoDePagamentosPage(pagamentoPage);
    }

    @Override
    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoCliente(UUID uuidEmpresaSessao,
                                                                            UUID uuidCliente,
                                                                            Pageable pageable) {

        log.info("Método de serviço de obtenção paginada de pagamentos do cliente acessado.");

        log.info("Acessando repositório de busca de pagamentos do cliente");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository
                .buscaPorPagamentosDoCliente(pageable, uuidEmpresaSessao, uuidCliente);

        return pagamentoServiceUtil.realizaConversaoDePagamentosPage(pagamentoPage);
    }

    @Override
    public PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoPlano(UUID uuidEmpresaSessao,
                                                                          UUID uuidPlano,
                                                                          Pageable pageable) {

        log.info("Método de serviço de obtenção paginada de pagamentos do plano acessado.");

        log.info("Acessando repositório de busca de pagamentos do plano");
        Page<PagamentoEntity> pagamentoPage = pagamentoRepository
                .buscaPorPagamentosDoPlano(pageable, uuidEmpresaSessao, uuidPlano);

        return pagamentoServiceUtil.realizaConversaoDePagamentosPage(pagamentoPage);
    }

    @Override
    public PagamentoResponse cancelaPagamento(UUID uuidEmpresaSessao,
                                              UUID uuidPagamento) {

        log.info("Método responsável por realizar o cancelamento de um pagamento acessado");

        log.info("Iniciando acesso ao método de implementação de busca de pagamento por id...");
        PagamentoEntity pagamento = pagamentoRepositoryImpl
                .implementaBuscaPorId(new PagamentoId(uuidEmpresaSessao, uuidPagamento));

        log.info("Iniciando acesso ao método de validação do cancelamento do pagamento atual...");
        pagamentoValidator.realizaValidacoesCancelamento(pagamento);

        log.info("Setando status do pagamento como removido...");
        pagamento.setStatusPagamento(StatusPagamentoEnum.CANCELADO);

        log.info("Implementando persistência do pagamento de assinatura atualizado...");
        PagamentoEntity pagamentoCancelado = pagamentoRepositoryImpl.implementaPersistencia(pagamento);

        log.info("Iniciando acesso ao método de cancelamento de pagamento na integradora ASAAS...");
        pagamentoAsaasProxyImpl.realizaCancelamentoDeCobrancaNaIntegradoraAsaas(pagamento);

        log.info("Iniciando construção do objeto pagamento response a partir da entidade gerada...");
        PagamentoResponse pagamentoResponse = new PagamentoResponse().constroiPagamentoResponse(pagamentoCancelado);

        log.info("Cancelamento do pagamento realizado com sucesso");
        return pagamentoResponse;
    }

    @Override
    public Double calculaValorTaxaPagamento(PagamentoEntity pagamento) {

        log.info("Método de cálculo de valor líquido do pagamento acessado");

        log.info("Iniciando setagem das variáveis do método...");
        Double valorBrutoPagamento = pagamento.getValorBruto();
        double taxaTotal = 0.0;

        log.info("Rodando estrutura condicional para direcionar taxa para sua forma de pagamento correspondente...");
        switch (pagamento.getFormaPagamento()) {
            case BOLETO -> taxaTotal
                    += (((valorBrutoPagamento / 100) * taxaAsaasBoletoPercentual) + taxaAsaasBoletoFixa);
            case CREDIT_CARD -> taxaTotal
                    += (((valorBrutoPagamento / 100) * taxaAsaasCartaoCreditoPercentual) + taxaAsaasCartaoCreditoFixa);
            default -> taxaTotal
                    += (((valorBrutoPagamento / 100) * taxaAsaasPixPercentual) + taxaAsaasPixFixa);
        }
        log.info("Taxa da integradora calculada: {}", taxaTotal);

        taxaTotal += (((valorBrutoPagamento / 100) * taxaSistemaPercentual) + taxaSistemaFixa);
        log.info("Taxa total calculada: {}", taxaTotal);

        log.info("Método executado com sucesso. Retornando valor líquido: {}...", valorBrutoPagamento - taxaTotal);
        return taxaTotal;
    }

    //TODO REMOVER. PARA TESTES
    public PlanoEntity injetaPagamentoNoPlano(UUID uuidEmpresa, UUID uuidPlano) {

        log.info("Método de criação de novo pagamento acessado");

        log.info("Iniciando obtenção do plano responsável pelo pagamento...");
        PlanoEntity planoPai = planoRepositoryImpl.implementaBuscaPorId(new PlanoId(uuidEmpresa, uuidPlano));
        log.info("Plano obtido com sucesso");

        log.info("Obtendo empresa...");
        EmpresaEntity empresaResponsavel = planoPai.getEmpresa();
        log.info("Empresa obtida com sucesso");

        PagamentoWebHookRequest pagamentoWebHookRequest = new PagamentoWebHookRequest();
        pagamentoWebHookRequest.setId("pagamento_123456");
        pagamentoWebHookRequest.setDueDate("2023-10-20");
        pagamentoWebHookRequest.setValue(200.0);
        pagamentoWebHookRequest.setNetValue(100.0);
        pagamentoWebHookRequest.setDescription("descrição");
        pagamentoWebHookRequest.setInvoiceUrl("url");
        pagamentoWebHookRequest.setBankSlipUrl("url");
        pagamentoWebHookRequest.setTransactionReceiptUrl("url");
        pagamentoWebHookRequest.setBillingType(BillingTypeEnum.BOLETO);

        log.info("Iniciando acesso ao método responsável pela conversão de objeto do tipo " +
                "PagamentoWebHookRequest para objeto do tipo PagamentoEntity...");
        PagamentoEntity pagamentoEntity = new PagamentoEntity()
                .constroiPagamentoEntityParaCriacao(empresaResponsavel, planoPai, pagamentoWebHookRequest);
        log.info(ConstantesPagamento.OBJETO_PAGAMENTO_CRIADO, pagamentoEntity);

        log.info("Acoplando pagamento ao plano pai...");
        planoPai.addPagamento(pagamentoEntity);
        log.info("Plano acoplado ao pagamento com sucesso");

        log.info("Iniciando persistência de pagamento acomplado no plano pai...");
        PlanoEntity planoEntity = planoRepositoryImpl.implementaPersistencia(planoPai);
        log.info("Persistência do plano pai com pagamento acoplado realizada com sucesso");

        return planoEntity;
    }

}
