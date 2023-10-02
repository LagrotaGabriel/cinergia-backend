package br.com.backend.modules.plano.repository.impl;

import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.exceptions.ObjectNotFoundException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlanoRepositoryImpl {

    @Autowired
    PlanoRepository planoRepository;

    @Transactional
    public PlanoEntity implementaPersistencia(PlanoEntity plano) {
        log.debug("Método de serviço que implementa persistência do plano acessado");
        return planoRepository.save(plano);
    }

    public PlanoEntity implementaBuscaPorId(Long id, Long idEmpresa) {
        log.debug("Método que implementa busca de plano por id acessado. Id: {}", id);

        Optional<PlanoEntity> planoOptional = planoRepository.buscaPorId(id, idEmpresa);

        PlanoEntity planoEntity;
        if (planoOptional.isPresent()) {
            planoEntity = planoOptional.get();
            log.debug(Constantes.PLANO_ENCONTRADO, planoEntity);
        } else {
            log.warn("Nenhum plano foi encontrado com o id {}", id);
            throw new ObjectNotFoundException("Nenhum plano foi encontrado com o id informado");
        }
        log.debug("Retornando o plano encontrado...");
        return planoEntity;
    }

    public List<PagamentoEntity> implementaBuscaDePagamentosDoPlanoPorId(Long id, Long idEmpresa) {
        log.debug("Método que implementa busca de pagamentos de plano por id acessado. Id: {}", id);

        Optional<PlanoEntity> planoOptional = planoRepository.buscaPorId(id, idEmpresa);

        PlanoEntity planoEntity;
        if (planoOptional.isPresent()) {
            planoEntity = planoOptional.get();
            log.debug(Constantes.PLANO_ENCONTRADO, planoEntity);
        } else {
            log.warn("Nenhum plano foi encontrado com o id {}", id);
            throw new ObjectNotFoundException("Nenhum plano foi encontrado com o id informado");
        }
        log.debug("Retornando os pagamentos do plano encontrado...");
        return planoEntity.getPagamentos();
    }

    public PlanoEntity implementaBuscaPorCodigoPlanoAsaas(String codigoPlanoAsaas) {
        log.debug("Método que implementa busca por plano Asaas pelo seu código de plano acessado");

        Optional<PlanoEntity> planoOptional =
                planoRepository.findByIdAsaas(codigoPlanoAsaas);

        PlanoEntity planoEntity;
        if (planoOptional.isPresent()) {
            planoEntity = planoOptional.get();
            log.debug(Constantes.PLANO_ENCONTRADO, planoEntity);
        } else {
            log.warn("Nenhum plano foi encontrado com o código Asaas informado: {}", codigoPlanoAsaas);
            throw new ObjectNotFoundException("Nenhum plano foi encontrado com o codigo Asaas informado");
        }

        log.debug("Retornando plano...");
        return planoEntity;
    }
}
