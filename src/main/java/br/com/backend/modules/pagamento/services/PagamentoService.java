package br.com.backend.modules.pagamento.services;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.models.responses.PagamentoResponse;
import br.com.backend.modules.pagamento.models.responses.page.PagamentoPageResponse;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PagamentoService {

    PagamentoPageResponse realizaBuscaPaginadaPorPagamentos(UUID uuidEmpresaSessao,
                                                            String campoBusca,
                                                            Pageable pageable);

    PagamentoPageResponse realizaBuscaPaginadaPorPagamentosRealizados(UUID uuidEmpresaSessao,
                                                                      Pageable pageable);

    PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoCliente(UUID uuidEmpresaSessao,
                                                                     UUID uuidCliente,
                                                                     Pageable pageable);

    PagamentoPageResponse realizaBuscaPaginadaPorPagamentosDoPlano(UUID uuidEmpresaSessao,
                                                                   UUID uuidPlano,
                                                                   Pageable pageable);

    PagamentoResponse cancelaPagamento(UUID uuidEmpresaSessao,
                                       UUID uuidPagamento);

    Double calculaValorTaxaPagamento(PagamentoEntity pagamento);

    //TODO REMOVER. PARA TESTES
    PlanoEntity injetaPagamentoNoPlano(UUID uuidEmpresaSessao,
                                       UUID uuidPlano);
}
