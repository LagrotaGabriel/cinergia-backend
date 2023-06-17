package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.response.PlanoPageResponse;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.repositories.plano.PlanoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoService {

    @Autowired
    PlanoRepository planoRepository;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    public PlanoPageResponse realizaBuscaPaginadaPorPlanosDoCliente(EmpresaEntity empresaLogada,
                                                           Pageable pageable,
                                                           Long idCliente) {
        log.debug("Método de serviço de obtenção paginada de planos do cliente acessado");

        log.debug("Acessando repositório de busca de planos do cliente");
        Page<PlanoEntity> planoPage = planoRepository.buscaPorPlanosDoCliente(pageable, empresaLogada.getId(), idCliente);

        log.debug("Busca de planos por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        PlanoPageResponse planoPageResponse = planoTypeConverter.converteListaDePlanosEntityParaPlanosResponse(planoPage);
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
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
        log.debug("Conversão de tipagem realizada com sucesso");

        log.info("A busca paginada de planos foi realizada com sucesso");
        return planoPageResponse;
    }
}
