package br.com.backend.modules.empresa.services.impl;

import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import br.com.backend.modules.empresa.models.dto.response.views.DadosDashBoardResponse;
import br.com.backend.modules.empresa.models.dto.response.views.DadosGraficoResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaResponse;
import br.com.backend.modules.empresa.models.dto.response.views.EmpresaSimplificadaResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.impl.EmpresaRepositoryImpl;
import br.com.backend.modules.empresa.services.EmpresaService;
import br.com.backend.modules.empresa.services.validator.EmpresaValidationService;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.pagamento.services.PagamentoService;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    EmpresaValidationService empresaValidationService;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public EmpresaResponse criaNovaEmpresa(EmpresaRequest empresaRequest) {

        log.info("Método de serviço responsável pelo tratamento do objeto recebido e criação de nova empresa acessado");

        log.info("Iniciando acesso ao método de validação de variáveis de chave única para empresa...");
        empresaValidationService.validaSeChavesUnicasJaExistemParaNovaEmpresa(empresaRequest);

        log.info("Iniciando construção do objeto EmpresaEntity...");
        EmpresaEntity empresaEntity = new EmpresaEntity().constroiEmpresaEntityParaCriacao(empresaRequest);
        log.info("Construção de objeto EmpresaEntity realizado com sucesso");

        log.info("Iniciando acesso ao método de implementação de persistência do cliente...");
        EmpresaEntity empresaPersistida = empresaRepositoryImpl.implementaPersistencia(empresaEntity);

        log.info("Empresa criada com sucesso");
        return new EmpresaResponse().constroiEmpresaResponse(empresaPersistida);
    }

    public void adicionaSaldoContaEmpresa(PagamentoEntity pagamento) {
        log.info("Método responsável por adicionar saldo à empresa após pagamento realizado acessado");

        EmpresaEntity empresa = pagamento.getEmpresa();

        //TODO VERIFICAR SE COM QUERY DE UPDATE FICA MELHOR
        log.info("Setando saldo através do método de cálculo de valor líquido do pagamento...");
        empresa.setSaldo(empresa.getSaldo()
                + (pagamento.getValorBruto() - pagamentoService.calculaValorTaxaPagamento(pagamento)));

        log.info("Iniciando persistência da empresa com o saldo atualizado...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
    }

    public EmpresaSimplificadaResponse obtemDadosSimplificadosEmpresa(UUID uuidEmpresaSessao) {
        log.info("Método de obtenção de dados simplificados da empresa acessado");
        //TODO UTILIZAR PROJEÇÃO PARA RETORNAR DADOS SIMPLIFICADOS DA EMPRESA

        log.info("Iniciando acesso ao método de busca de empresa por id...");
        EmpresaEntity empresa = empresaRepositoryImpl.implementaBuscaPorId(uuidEmpresaSessao);
        log.info("Busca de empresa por id realizada com sucesso");

        log.info("Retornando dados simplificados da empresa...");
        return EmpresaSimplificadaResponse.builder()
                .nomeEmpresa(empresa.getNomeEmpresa())
                .saldo(empresa.getSaldo())
                .build();
    }

    public void realizaAtualizacaoSaldoEmpresaParaTransferencia(EmpresaEntity empresa,
                                                                TransferenciaRequest transferenciaRequest) {
        //TODO VERIFICAR SE SALDO DA EMPRESA ESTÁ SENDO ATUALIZADO. EXPLORAR QUERY UPDATE PARA ATUALIZAÇÃO DE SALDO
        log.info("Método responsável por realizar atualização do saldo da empresa após realização de transferência acessado");

        log.info("Iniciando acesso ao método de validação se empresa possui saldo suficiente para realizar " +
                "transferência desejada...");
        empresaValidationService.validaSeEmpresaPossuiSaldoSuficienteParaRealizarTransferencia(
                empresa, transferenciaRequest.getValor());
        log.info("Validação concluída com sucesso");

        log.info("Atualizando saldo da empresa...");
        empresa.setSaldo(empresa.getSaldo() - transferenciaRequest.getValor());
        log.info("Saldo da empresa atualizado com sucesso");

        log.info("Iniciando persistência da empresa com o saldo atualizado...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
    }

    public DadosDashBoardResponse obtemDadosDashBoardEmpresa(UUID uuidEmpresaSessao) {
        log.info("Método responsável por obter os dados de dashboard da empresa acessado");
        //TODO REATIVAR

        log.info("Iniciando acesso ao método de implementação de busca de empresa por id...");
        EmpresaEntity empresa = empresaRepositoryImpl.implementaBuscaPorId(uuidEmpresaSessao);
        log.info("Empresa encontrada com sucesso");

        log.info("Iniciando obtenção de pagamentos atrasados...");
        Double pagamentosAtrasados = pagamentoRepository.calculaTotalCobrancasAtrasadasDaEmpresa(uuidEmpresaSessao);
        log.info("Obtenção de pagamentos atrasados realizada com sucesso: {}", pagamentosAtrasados);

        log.info("Iniciando obtenção de pagamentos previstos...");
        Double pagamentosPendentes = pagamentoRepository.calculaTotalCobrancasPendentesDaEmpresa(uuidEmpresaSessao);
        log.info("Obtenção de pagamentos previstos realizada com sucesso: {}", pagamentosPendentes);

        log.info("Iniciando obtenção de pagamentos confirmados...");
        Double pagamentosConfirmados = pagamentoRepository.calculaTotalCobrancasAprovadasDaEmpresa(uuidEmpresaSessao);
        log.info("Obtenção de pagamentos confirmados realizada com sucesso: {}", pagamentosConfirmados);

        log.info("Iniciando obtenção de quantidade de assinaturas ativas...");
        Integer qtdPlanosAtivos = planoRepository.somaQuantidadeDeAssinaturasAtivasDaEmpresa(uuidEmpresaSessao);
        log.info("Obtenção de quantidade de planos ativos realizada com sucesso: {}", qtdPlanosAtivos);

        log.info("Iniciando obtenção de quantidade de assinaturas inativas...");
        Integer qtdPlanosInativos = planoRepository.somaQuantidadeDeAssinaturasInativasDaEmpresa(uuidEmpresaSessao);
        log.info("Obtenção de quantidade de planos inativos realizada com sucesso: {}", qtdPlanosInativos);

        log.info("Iniciando construção do objeto DadosDashBoardResponse...");
        DadosDashBoardResponse dadosDashBoardResponse = DadosDashBoardResponse.builder()
                .saldo(empresa.getSaldo() != null
                        ? ConversorDeDados.converteValorDoubleParaValorMonetario(empresa.getSaldo())
                        : Constantes.ZERO_REAIS)
                .atrasado(pagamentosAtrasados != null
                        ? ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentosAtrasados)
                        : Constantes.ZERO_REAIS)
                .previsto(pagamentosPendentes != null
                        ? ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentosPendentes)
                        : Constantes.ZERO_REAIS)
                .confirmado(pagamentosConfirmados != null
                        ? ConversorDeDados.converteValorDoubleParaValorMonetario(pagamentosConfirmados)
                        : Constantes.ZERO_REAIS)
                .qtdAssinaturasAtivas(qtdPlanosAtivos != null
                        ? qtdPlanosAtivos
                        : 0)
                .qtdAssinaturasInativas(qtdPlanosInativos != null
                        ? qtdPlanosInativos
                        : 0)
                .totalAssinaturas(qtdPlanosAtivos != null && qtdPlanosInativos != null
                        ? qtdPlanosAtivos + qtdPlanosInativos
                        : 0)
                .build();
        log.info("Objeto DadosDashBoardResponse construído com sucesso. Retornando...");
        return dadosDashBoardResponse;
    }

    public Map<Integer, DadosGraficoResponse> obtemDadosGraficoFaturamentoEmpresa(UUID uuidEmpresaSessao) {

        //TODO OTIMIZAR GRÁFICOS COM QUERYS DE VALORES

        LocalDate dataAgora = LocalDate.now();
        int anoAtual = dataAgora.getYear();

        List<PagamentoEntity> pagamentosDoAno = pagamentoRepository.buscaTodosPagamentosEmpresa(uuidEmpresaSessao)
                .stream()
                .filter(p -> p.getDataPagamento() != null)
                .filter(p -> {
                    LocalDate dataPagamentoConvertida = LocalDate.parse(p.getDataPagamento());
                    return dataPagamentoConvertida.getYear() == anoAtual
                            && p.getStatusPagamento().equals(StatusPagamentoEnum.APROVADO);
                }).toList();

        HashMap<Integer, DadosGraficoResponse> faturamentoPorMes = new HashMap<>();

        for (int i = 1; i < 13; i++) {
            int mesIterado = i;

            List<PagamentoEntity> pagamentosDoMes = pagamentosDoAno
                    .stream()
                    .filter(p -> {
                        LocalDate dataPagamentoConvertida = LocalDate.parse(p.getDataPagamento());
                        return dataPagamentoConvertida.getMonthValue() == mesIterado
                                && p.getStatusPagamento().equals(StatusPagamentoEnum.APROVADO);
                    }).toList();

            Double somaValorBrutoPagamentos =
                    pagamentosDoMes
                            .stream()
                            .mapToDouble(PagamentoEntity::getValorBruto).sum();

            Double somaValorTaxaPagamentos =
                    pagamentosDoMes
                            .stream()
                            .mapToDouble(PagamentoEntity::getTaxaTotal).sum();

            Double valorLiquidoPagamentos = (somaValorBrutoPagamentos - somaValorTaxaPagamentos);

            DadosGraficoResponse dadosGraficoResponse = DadosGraficoResponse.builder()
                    .valorBruto(somaValorBrutoPagamentos)
                    .valorTaxa(somaValorTaxaPagamentos)
                    .valorLiquido(valorLiquidoPagamentos)
                    .build();

            faturamentoPorMes.put(mesIterado, dadosGraficoResponse);
        }

        return faturamentoPorMes;
    }
}
