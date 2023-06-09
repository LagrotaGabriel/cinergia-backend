package br.com.backend.repositories.empresa.impl;

import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.repositories.empresa.EmpresaRepository;
import br.com.backend.services.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<EmpresaEntity> implementaBuscaPorCpfCnpjIdentico(String cpfCnpj) {
        log.debug("Método que implementa busca de cliente por CPF/CNPJ acessado. CPF/CNPJ: {}", cpfCnpj);
        return repository.buscaPorCpfCnpjIdentico(cpfCnpj);
    }

    public EmpresaEntity implementaBuscaPorId(Long id) {
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

}
