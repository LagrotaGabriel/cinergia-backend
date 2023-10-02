package br.com.backend.modules.plano.services;

import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.models.dto.response.DadosPlanoResponse;
import br.com.backend.modules.plano.models.dto.response.PlanoPageResponse;
import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.plano.models.enums.StatusPlanoEnum;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.cliente.repository.impl.ClienteRepositoryImpl;
import br.com.backend.modules.plano.proxy.impl.PlanoAsaasProxyImpl;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.plano.repository.impl.PlanoRepositoryImpl;
import br.com.backend.exceptions.InvalidRequestException;
import br.com.backend.modules.plano.services.adapter.PlanoTypeConverter;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlanoService {

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PlanoRepositoryImpl planoRepositoryImpl;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    @Autowired
    PlanoAsaasProxyImpl planoAsaasProxyImpl;

    @Transactional
    public PlanoResponse criaNovoPlano(EmpresaEntity empresaLogada, Long idCliente, PlanoRequest planoRequest) {

        log.debug("Método de serviço de criação de novo plano acessado");

        log.debug("Iniciando acesso ao método de busca de cliente por id com o id {} na empresa de id {}...",
                idCliente, empresaLogada.getId());
        ClienteEntity clienteEncontrado = clienteRepositoryImpl.implementaBuscaPorId(idCliente, empresaLogada.getId());

        log.debug("Iniciando criação do objeto PlanoEntity...");
        PlanoEntity planoEntity = PlanoEntity.builder()
                .idEmpresaResponsavel(empresaLogada.getId())
                .idClienteResponsavel(idCliente)
                .idAsaas((planoRequest.getFormaPagamento() == FormaPagamentoEnum.PIX || planoRequest.getFormaPagamento() == FormaPagamentoEnum.BOLETO)
                        ? planoAsaasProxyImpl.realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(planoRequest, clienteEncontrado)
                        : null)
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .dataInicio(planoRequest.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(StatusPlanoEnum.INATIVO)
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .pagamentos(new ArrayList<>())
                .build();
        log.debug("Objeto planoEntity criado com sucesso");

        log.debug("Acoplando plano ao cliente encontrado...");
        clienteEncontrado.getPlanos().add(planoEntity);

        log.debug("Iniciando acesso ao método de implementação da persistência do cliente com o novo plano acoplado...");
        ClienteEntity clientePersistido = clienteRepositoryImpl.implementaPersistencia(clienteEncontrado);

        log.debug("Atualização do saldo da empresa logada realizada com sucesso");

        log.debug("Iniciando obtenção do plano criado...");
        PlanoEntity planoPersistido = clientePersistido.getPlanos().get(clientePersistido.getPlanos().size() - 1);

        log.debug("Convertendo planoEntity criado para planoResponse...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoPersistido);

        log.info("Plano criado com sucesso");
        return planoResponse;
    }

    @Transactional
    public PlanoResponse cancelaAssinatura(Long idAssinatura, EmpresaEntity empresa) {

        log.debug("Método responsável por realizar o cancelamento de uma assinatura acessado. ID: {}", idAssinatura);
        PlanoEntity plano = planoRepositoryImpl.implementaBuscaPorId(idAssinatura, empresa.getId());

        if (plano.getStatusPlano().equals(StatusPlanoEnum.REMOVIDO))
            throw new InvalidRequestException("Não é possível remover um plano que já foi removido");

        log.debug("Iniciando acesso ao método de cancelamento de assinatura na integradora ASAAS...");
        planoAsaasProxyImpl.realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(plano);

        log.debug("Setando status do plano como removido...");
        plano.setStatusPlano(StatusPlanoEnum.REMOVIDO);

        log.debug("Implementando persistência do plano de assinatura atualizado...");
        PlanoResponse planoResponse =
                planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoRepositoryImpl.implementaPersistencia(plano));

        log.info("Cancelamento da assinatura realizado com sucesso");
        return planoResponse;
    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(EmpresaEntity empresaLogada,
                                                                    Pageable pageable,
                                                                    Long idCliente) {
        log.debug("Método de serviço de obtenção paginada de planos do cliente acessado");

        log.debug("Acessando repositório de busca de planos do cliente");
        Page<PlanoEntity> planoPage = planoRepository.buscaPorPlanosDoCliente(pageable, empresaLogada.getId(), idCliente);

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    public PlanoResponse atualizaPlano(EmpresaEntity empresaLogada, PlanoRequest planoRequest) {
        log.debug("Método de serviço de atualização de plano acessado");

        log.debug(Constantes.BUSCA_PLANO_POR_ID);
        PlanoEntity planoEncontrado = planoRepositoryImpl.implementaBuscaPorId(planoRequest.getId(), empresaLogada.getId());

        log.debug("Iniciando acesso ao método de validação de alteração de dados de plano excluído...");
        validaSePlanoEstaExcluido(planoEncontrado, "Não é possível atualizar um plano excluído");

        log.debug("Iniciando criação do objeto PlanoEntity...");
        PlanoEntity novoPlanoAtualizado = PlanoEntity.builder()
                .id(planoEncontrado.getId())
                .idEmpresaResponsavel(planoEncontrado.getIdEmpresaResponsavel())
                .idClienteResponsavel(planoEncontrado.getIdClienteResponsavel())
                .idAsaas(planoEncontrado.getIdAsaas())
                .dataCadastro(planoEncontrado.getDataCadastro())
                .horaCadastro(planoEncontrado.getHoraCadastro())
                .dataVencimento(planoEncontrado.getDataVencimento())
                .dataInicio(planoRequest.getDataInicio())
                .descricao(planoRequest.getDescricao())
                .valor(planoRequest.getValor())
                .formaPagamento(planoRequest.getFormaPagamento())
                .statusPlano(planoEncontrado.getStatusPlano())
                .periodicidade(planoRequest.getPeriodicidade())
                .notificacoes(planoRequest.getNotificacoes())
                .pagamentos(planoEncontrado.getPagamentos())
                .build();
        log.debug("Objeto plano construído com sucesso");

        log.debug("Iniciando processo de atualização do plano na integradora ASAAS...");
        planoAsaasProxyImpl.realizaAtualizacaoDePlanoDeAssinaturaNaIntegradoraAsaas(
                novoPlanoAtualizado.getIdAsaas(), novoPlanoAtualizado);

        log.debug("Iniciando acesso ao método de implementação da persistência do plano...");
        PlanoEntity planoPersistido = planoRepositoryImpl.implementaPersistencia(novoPlanoAtualizado);

        log.debug("Plano persistido com sucesso. Convertendo PlanoEntity para PlanoResponse...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(planoPersistido);

        log.info("Plano atualizado com sucesso");
        return planoResponse;
    }

    public PlanoPageResponse realizaBuscaPaginadaPorPlanos(EmpresaEntity empresaLogada,
                                                           Pageable pageable,
                                                           String campoBusca) {
        log.debug("Método de serviço de obtenção paginada de planos acessado. Campo de busca: {}",
                campoBusca != null ? campoBusca : "Nulo");

        log.debug("Acessando repositório de busca de planos");
        Page<PlanoEntity> planoPage = campoBusca != null && !campoBusca.isEmpty()
                ? planoRepository.buscaPorPlanosTypeAhead(pageable, campoBusca, empresaLogada.getId())
                : planoRepository.buscaPorPlanos(pageable, empresaLogada.getId());

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }

    public PlanoResponse realizaBuscaDePlanoPorId(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de obtenção de plano por id. ID recebido: {}", id);

        log.debug("Acessando repositório de busca de plano por ID...");
        PlanoEntity plano = planoRepositoryImpl.implementaBuscaPorId(id, empresaLogada.getId());

        log.debug("Busca de plano por id realizada com sucesso. Acessando método de conversão dos objeto do tipo " +
                "Entity para objeto do tipo Response...");
        PlanoResponse planoResponse = planoTypeConverter.convertePlanoEntityParaPlanoResponse(plano);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca de plano por id foi realizada com sucesso");
        return planoResponse;
    }

    public DadosPlanoResponse realizaBuscaDeDadosDePlanoPorId(EmpresaEntity empresaLogada, Long id) {
        log.debug("Método de serviço de obtenção de dados de um plano por id. ID recebido: {}", id);

        log.debug("Acessando repositório de busca de pagamentos de um plano por ID...");
        List<PagamentoEntity> pagamentos = planoRepositoryImpl.implementaBuscaDePagamentosDoPlanoPorId(id, empresaLogada.getId());

        log.debug("Busca de pagamentos do plano por id realizada com sucesso. Acessando método de cálculo de dados...");
        DadosPlanoResponse dadosPlanoResponse = calculaDadosDoPlano(pagamentos);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca de plano por id foi realizada com sucesso");
        return dadosPlanoResponse;
    }

    private DadosPlanoResponse calculaDadosDoPlano(List<PagamentoEntity> pagamentos) {

        log.debug("Método responsável por calcular os dados de pagamentos do plano acessado");

        log.debug("Iniciando criação das variáveis...");
        double quantidadeCobrancas = 0.0;
        Double totalCobrancas = 0.0;

        Double totalPendente = 0.0;

        Double totalPago = 0.0;

        double quantidadeEmAtraso = 0.0;
        Double totalEmAtraso = 0.0;

        double comprometimento = 100.0;

        for (PagamentoEntity pagamento : pagamentos) {
            quantidadeCobrancas++;
            totalCobrancas += pagamento.getValorBruto();

            if (pagamento.getStatusPagamento() == StatusPagamentoEnum.APROVADO) {
                totalPago += pagamento.getValorBruto();
            } else if (pagamento.getStatusPagamento() == StatusPagamentoEnum.PENDENTE) {
                totalPendente += pagamento.getValorBruto();
            } else {
                quantidadeEmAtraso++;
                totalEmAtraso += pagamento.getValorBruto();
            }

        }

        if (quantidadeCobrancas > 0.0) comprometimento -= ((quantidadeEmAtraso * 100) / (quantidadeCobrancas));

        log.debug("Iniciando construção do objeto DadosPlanoResponse...");
        DadosPlanoResponse dadosPlanoResponse = DadosPlanoResponse.builder()
                .totalCobrancas(totalCobrancas)
                .totalPendente(totalPendente)
                .totalPago(totalPago)
                .totalEmAtraso(totalEmAtraso)
                .comprometimento(comprometimento)
                .build();

        log.debug("Objeto construído com sucesso. Retornando objeto...");
        return dadosPlanoResponse;
    }

    public void validaSePlanoEstaExcluido(PlanoEntity planoEntity, String mensagemCasoEstejaExcluido) {
        log.debug("Método de validação de planoEntity excluído acessado");
        if (planoEntity.getStatusPlano().equals(StatusPlanoEnum.REMOVIDO)) {
            log.debug("Plano de id {}: Validação de planoEntity já excluído falhou. Não é possível realizar operações " +
                    "em um planoEntity que já foi excluído.", planoEntity.getId());
            throw new InvalidRequestException(mensagemCasoEstejaExcluido);
        }
        log.debug("O planoEntity de id {} não está excluído", planoEntity.getId());
    }

}
