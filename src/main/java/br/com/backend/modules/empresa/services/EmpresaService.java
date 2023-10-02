package br.com.backend.modules.empresa.services;

import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import br.com.backend.modules.empresa.models.dto.response.DadosDashBoardResponse;
import br.com.backend.modules.empresa.models.dto.response.DadosGraficoResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaSimplificadaResponse;
import br.com.backend.modules.empresa.services.adapter.EmpresaTypeConverter;
import br.com.backend.modules.empresa.services.validator.EmpresaValidationService;
import br.com.backend.globals.models.acesso.entity.AcessoSistemaEntity;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.globals.models.acesso.enums.PerfilEnum;
import br.com.backend.modules.pagamento.models.enums.StatusPagamentoEnum;
import br.com.backend.modules.empresa.repository.impl.EmpresaRepositoryImpl;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.pagamento.services.PagamentoService;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmpresaService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    EmpresaValidationService empresaValidationService;

    @Autowired
    EmpresaTypeConverter empresaTypeConverter;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public EmpresaResponse criaNovaEmpresa(EmpresaRequest empresaRequest) {

        log.debug("Método de serviço responsável pelo tratamento do objeto recebido e criação de nova empresa acessado");

        log.debug("Iniciando acesso ao método de validação de variáveis de chave única para empresa...");
        empresaValidationService.validaSeChavesUnicasJaExistemParaNovaEmpresa(empresaRequest);

        log.debug("Criando hashset com perfil empresa...");
        Set<PerfilEnum> perfis = new HashSet<>();
        perfis.add(PerfilEnum.EMPRESA);

        log.debug("Iniciando construção do objeto EmpresaEntity...");
        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .saldo(0.0)
                .nomeEmpresa(empresaRequest.getNomeEmpresa())
                .email(empresaRequest.getEmail())
                .cpfCnpj(empresaRequest.getCpfCnpj()
                        .replace("-", "")
                        .replace("/", "")
                        .replace(".", "")
                        .trim())
                .dataNascimento(empresaRequest.getDataNascimento())
                .endereco(empresaRequest.getEndereco())
                .telefone(empresaRequest.getTelefone())
                .celular(empresaRequest.getCelular())
                .acessoSistema(AcessoSistemaEntity.builder()
                        .senha(new BCryptPasswordEncoder().encode(empresaRequest.getAcessoSistema().getSenha()))
                        .perfis(perfis)
                        .build())
                .logoEmpresa(null)
                .clientes(new ArrayList<>())
                .build();
        log.debug("Construção de objeto EmpresaEntity realizado com sucesso");

        log.debug("Iniciando acesso ao método de implementação de persistência do cliente...");
        EmpresaEntity empresaPersistida = empresaRepositoryImpl.implementaPersistencia(empresaEntity);

        log.info("Empresa criada com sucesso");
        return empresaTypeConverter.converteEmpresaEntityParaEmpresaResponse(empresaPersistida);
    }

    public void adicionaSaldoContaEmpresa(EmpresaEntity empresa, PagamentoEntity pagamento) {
        log.debug("Método responsável por adicionar saldo à empresa após pagamento realizado acessado");

        log.debug("Setando saldo através do método de cálculo de valor líquido do pagamento...");
        empresa.setSaldo(empresa.getSaldo() + (pagamento.getValorBruto() - pagamentoService.calculaValorTaxaPagamento(pagamento)));

        log.debug("Iniciando persistência da empresa com o saldo atualizado...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
    }

    public EmpresaSimplificadaResponse obtemDadosSimplificadosEmpresa(EmpresaEntity empresa) {
        return EmpresaSimplificadaResponse.builder()
                .nomeEmpresa(empresa.getNomeEmpresa())
                .saldo(empresa.getSaldo())
                .build();
    }

    public DadosDashBoardResponse obtemDadosDashBoardEmpresa(EmpresaEntity empresa) {

        log.debug("Método responsável por obter os dados de dashboard da empresa acessado");

        log.debug("Iniciando obtenção de pagamentos atrasados...");
        Double pagamentosAtrasados = pagamentoRepository.somaDePagamentosAtrasados(empresa.getId());
        log.debug("Obtenção de pagamentos atrasados realizada com sucesso: {}", pagamentosAtrasados);

        log.debug("Iniciando obtenção de pagamentos previstos...");
        Double pagamentosPendentes = pagamentoRepository.somaDePagamentosPrevistos(empresa.getId());
        log.debug("Obtenção de pagamentos previstos realizada com sucesso: {}", pagamentosPendentes);

        log.debug("Iniciando obtenção de pagamentos confirmados...");
        Double pagamentosConfirmados = pagamentoRepository.somaDePagamentosConfirmados(empresa.getId());
        log.debug("Obtenção de pagamentos confirmados realizada com sucesso: {}", pagamentosConfirmados);

        log.debug("Iniciando obtenção de quantidade de assinaturas ativas...");
        Integer qtdPlanosAtivos = planoRepository.somaQtdAssinaturasAtivas(empresa.getId());
        log.debug("Obtenção de quantidade de planos ativos realizada com sucesso: {}", qtdPlanosAtivos);

        log.debug("Iniciando obtenção de quantidade de assinaturas inativas...");
        Integer qtdPlanosInativos = planoRepository.somaQtdAssinaturasInativas(empresa.getId());
        log.debug("Obtenção de quantidade de planos inativos realizada com sucesso: {}", qtdPlanosInativos);

        log.debug("Iniciando construção do objeto DadosDashBoardResponse...");
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
        log.debug("Objeto DadosDashBoardResponse construído com sucesso. Retornando...");
        return dadosDashBoardResponse;
    }

    public Map<Integer, DadosGraficoResponse> obtemDadosGraficoFaturamentoEmpresa(EmpresaEntity empresa) {

        LocalDate dataAgora = LocalDate.now();
        int anoAtual = dataAgora.getYear();

        List<PagamentoEntity> pagamentosDoAno = pagamentoRepository.buscaTodosPagamentosEmpresa(empresa.getId())
                .stream()
                .filter(p -> p.getDataPagamento() != null)
                .filter(p -> {
                    LocalDate dataPagamentoConvertida = LocalDate.parse(p.getDataPagamento());
                    return dataPagamentoConvertida.getYear() == anoAtual
                            && p.getStatusPagamento().equals(StatusPagamentoEnum.APROVADO);
                }).collect(Collectors.toList());

        HashMap<Integer, DadosGraficoResponse> faturamentoPorMes = new HashMap<>();

        for (int i = 1; i < 13; i++) {
            int mesIterado = i;

            List<PagamentoEntity> pagamentosDoMes = pagamentosDoAno
                    .stream()
                    .filter(p -> {
                        LocalDate dataPagamentoConvertida = LocalDate.parse(p.getDataPagamento());
                        return dataPagamentoConvertida.getMonthValue() == mesIterado
                                && p.getStatusPagamento().equals(StatusPagamentoEnum.APROVADO);
                    }).collect(Collectors.toList());

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
