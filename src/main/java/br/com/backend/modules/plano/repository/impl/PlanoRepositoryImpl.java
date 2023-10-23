package br.com.backend.modules.plano.repository.impl;

import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.modules.plano.models.entity.PlanoEntity;
import br.com.backend.modules.plano.models.entity.id.PlanoId;
import br.com.backend.modules.plano.repository.PlanoRepository;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PlanoRepositoryImpl {

    @Autowired
    PlanoRepository planoRepository;

    public PlanoEntity implementaPersistencia(PlanoEntity plano) {
        log.info("Método de serviço que implementa persistência do plano acessado");
        return planoRepository.save(plano);
    }

    public PlanoEntity implementaBuscaPorId(PlanoId planoId) {
        log.info("Método que implementa busca de plano por id acessado. Id: {}", planoId);

        Optional<PlanoEntity> planoOptional = planoRepository.findById(planoId);

        PlanoEntity planoEntity;
        if (planoOptional.isPresent()) {
            planoEntity = planoOptional.get();
            log.info(ConstantesPlano.PLANO_ENCONTRADO, planoEntity);
        } else {
            log.warn("Nenhum plano foi encontrado com o id {}", planoId);
            throw new ObjectNotFoundException("Nenhum plano foi encontrado com o id informado");
        }
        log.info("Retornando o plano encontrado...");
        return planoEntity;
    }

    public PlanoEntity implementaBuscaPorCodigoPlanoAsaas(String codigoPlanoAsaas) {
        log.info("Método que implementa busca por plano Asaas pelo seu código de plano acessado");

        Optional<PlanoEntity> planoOptional =
                planoRepository.findByAsaasId(codigoPlanoAsaas);

        PlanoEntity planoEntity;
        if (planoOptional.isPresent()) {
            planoEntity = planoOptional.get();
            log.info(ConstantesPlano.PLANO_ENCONTRADO, planoEntity);
        } else {
            log.warn("Nenhum plano foi encontrado com o código Asaas informado: {}", codigoPlanoAsaas);
            throw new ObjectNotFoundException("Nenhum plano foi encontrado com o codigo Asaas informado");
        }

        log.info("Retornando plano...");
        return planoEntity;
    }
}
