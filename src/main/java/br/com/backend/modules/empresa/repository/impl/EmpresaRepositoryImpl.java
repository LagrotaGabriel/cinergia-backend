package br.com.backend.modules.empresa.repository.impl;

import br.com.backend.exceptions.custom.UnauthorizedAccessException;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.EmpresaRepository;
import br.com.backend.exceptions.custom.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EmpresaRepositoryImpl {

    @Autowired
    EmpresaRepository repository;

    @Transactional
    public EmpresaEntity implementaPersistencia(EmpresaEntity empresa) {
        log.debug("Método de serviço que implementa persistência do empresa acessado");
        return repository.save(empresa);
    }

    public EmpresaEntity implementaBuscaPorId(UUID id) {
        log.debug("Método que implementa busca de empresa por id acessado. Id: {}", id);

        Optional<EmpresaEntity> empresaOptional = repository.findById(id);

        EmpresaEntity empresaEntity;
        if (empresaOptional.isPresent()) {
            empresaEntity = empresaOptional.get();
            log.debug("Empresa encontrada: {}", empresaEntity);
        } else {
            log.warn("Nenhuma empresa foi encontrada com o id {}", id);
            throw new ObjectNotFoundException("Nenhuma empresa foi encontrada com o id informado");
        }
        log.debug("Retornando a empresa encontrada...");
        return empresaEntity;
    }

    public EmpresaEntity realizaBuscaDeEmpresaLogadaPorUuid(UUID uuidEmpresaSessao) {
        return repository.findById(uuidEmpresaSessao)
                .orElseThrow(() -> {
                    log.error("Nenhuma empresa foi encontrada através do id informado pelo token: {}", uuidEmpresaSessao);
                    return new UnauthorizedAccessException("O token enviado é inválido");
                });
    }

}
