package br.com.backend.modules.cliente.services.validator;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.cliente.models.dto.request.ClienteRequest;
import br.com.backend.modules.cliente.models.entity.ClienteEntity;
import br.com.backend.modules.cliente.repository.ClienteRepository;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ClienteValidationService {

    @Autowired
    ClienteRepository clienteRepository;

    public void validaSeCpfCnpjJaExiste(UUID uuidEmpresa, String cpfCnpj) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");

        if (Boolean.TRUE.equals(clienteRepository.existsByEmpresaUuidAndCpfCnpj(uuidEmpresa, cpfCnpj))) {
            String mensagemErro = cpfCnpj.length() == 14
                    ? "O CPF informado já existe"
                    : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public void validaSeEmailJaExiste(UUID uuidEmpresa, String email) {
        log.debug("Método de validação de chave única de e-mail acessado");

        if (Boolean.TRUE.equals(clienteRepository.existsByEmpresaUuidAndEmail(uuidEmpresa, email))) {
            log.warn("O e-mail informado já existe: {}", email);
            throw new InvalidRequestException("O e-mail informado já existe");
        }
        log.debug("Validação de chave única de e-mail... OK");
    }

    public void validaSeChavesUnicasJaExistemParaNovoCliente(UUID uuidEmpresa, ClienteRequest clienteRequest) {
        log.debug("Método de validação de chave única de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null)
            validaSeCpfCnpjJaExiste(uuidEmpresa, clienteRequest.getCpfCnpj());
        if (clienteRequest.getEmail() != null)
            validaSeEmailJaExiste(uuidEmpresa, clienteRequest.getEmail());
    }

    public void validaSeChavesUnicasJaExistemParaClienteAtualizado(UUID uuidEmpresaSessao,
                                                                   ClienteRequest clienteRequest,
                                                                   ClienteEntity clienteEntity) {
        log.debug("Método de validação de chave única para atualização de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null && clienteEntity.getCpfCnpj() == null
                || clienteRequest.getCpfCnpj() != null
                && !clienteEntity.getCpfCnpj().equals(clienteRequest.getCpfCnpj()))
            validaSeCpfCnpjJaExiste(uuidEmpresaSessao, clienteRequest.getCpfCnpj());
        if (clienteRequest.getEmail() != null && clienteEntity.getEmail() == null
                || clienteRequest.getEmail() != null
                && !clienteEntity.getEmail().equals(clienteRequest.getEmail()))
            validaSeEmailJaExiste(uuidEmpresaSessao, clienteRequest.getCpfCnpj());
    }

}
