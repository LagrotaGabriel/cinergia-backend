package br.com.backend.modules.empresa.services;

import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import br.com.backend.modules.empresa.models.dto.response.views.DadosDashBoardResponse;
import br.com.backend.modules.empresa.models.dto.response.views.DadosGraficoResponse;
import br.com.backend.modules.empresa.models.dto.response.EmpresaResponse;
import br.com.backend.modules.empresa.models.dto.response.views.EmpresaSimplificadaResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.UUID;

public interface EmpresaService {

    EmpresaResponse criaNovaEmpresa(EmpresaRequest empresaRequest);

    @Async
    void adicionaSaldoContaEmpresa(PagamentoEntity pagamento);

    EmpresaSimplificadaResponse obtemDadosSimplificadosEmpresa(UUID uuidEmpresaSessao);

    void realizaAtualizacaoSaldoEmpresaParaTransferencia(EmpresaEntity empresa,
                                                         TransferenciaRequest transferenciaRequest);

    DadosDashBoardResponse obtemDadosDashBoardEmpresa(UUID uuidEmpresaSessao);

    Map<Integer, DadosGraficoResponse> obtemDadosGraficoFaturamentoEmpresa(UUID uuidEmpresaSessao);

}
