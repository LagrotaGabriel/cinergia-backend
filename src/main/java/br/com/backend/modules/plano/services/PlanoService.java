package br.com.backend.modules.plano.services;

import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.models.dto.response.DadosPlanoResponse;
import br.com.backend.modules.plano.models.dto.response.page.PlanoPageResponse;
import br.com.backend.modules.plano.models.dto.response.PlanoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PlanoService {

    @Transactional
    PlanoResponse criaNovoPlano(UUID uuidEmpresaSessao,
                                UUID uuidCliente,
                                PlanoRequest planoRequest);

    PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(Pageable pageable,
                                                             UUID uuidEmpresaSessao,
                                                             UUID uuidCliente);

    PlanoPageResponse realizaBuscaPaginadaPorPlanos(Pageable pageable,
                                                    UUID uuidEmpresaSessao,
                                                    String campoBusca);

    PlanoResponse realizaBuscaDePlanoPorId(UUID uuidEmpresaSessao,
                                           UUID uuidPlano);

    DadosPlanoResponse realizaBuscaDeDadosDePlanoPorId(UUID uuidEmpresaSessao,
                                                       UUID uuidPlano);

    PlanoResponse atualizaPlano(UUID uuidEmpresaSessao,
                                UUID uuidPlano,
                                PlanoRequest planoRequest);

    @Transactional
    PlanoResponse cancelaAssinatura(UUID uuidEmpresaSessao,
                                           UUID uuidPlano);

    void removePlanosDoCliente(UUID uuidCliente);

}
