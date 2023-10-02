package br.com.backend.modules.transferencia.repository.impl;

import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.exceptions.ObjectNotFoundException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TransferenciaRepositoryImpl {

    @Autowired
    TransferenciaRepository transferenciaRepository;

    public TransferenciaEntity implementaBuscaPorCodigoTransferenciaAsaas(String codigoTransferenciaAsaas) {
        log.debug("Método que implementa busca por transferência Asaas pelo seu código de transferência acessado");

        Optional<TransferenciaEntity> transferenciaOptional =
                transferenciaRepository.findByAsaasId(codigoTransferenciaAsaas);

        TransferenciaEntity transferenciaEntity;
        if (transferenciaOptional.isPresent()) {
            transferenciaEntity = transferenciaOptional.get();
            log.debug(Constantes.TRANSFERENCIA_ENCONTRADA, transferenciaEntity);
        } else {
            log.warn("Nenhuma transferência foi encontrada com o código Asaas informado: {}", codigoTransferenciaAsaas);
            throw new ObjectNotFoundException("Nenhuma transferência foi encontrada com o codigo Asaas informado");
        }

        log.debug("Retornando transferência...");
        return transferenciaEntity;
    }

}
