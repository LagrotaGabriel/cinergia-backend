package br.com.backend.services.cliente;

import br.com.backend.models.dto.cliente.request.ClienteRequest;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.repositories.cliente.impl.ClienteRepositoryImpl;
import br.com.backend.services.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClienteValidationService {

    @Autowired
    ClienteRepositoryImpl clienteRepositoryImpl;

    public void validaSeCpfCnpjJaExiste(String cpfCnpj, Long idEmpresa) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");
        if (clienteRepositoryImpl.implementaBuscaPorCpfCnpjIdentico(cpfCnpj, idEmpresa).isPresent()) {
            String mensagemErro = cpfCnpj.length() == 11 ? "O CPF informado já existe" : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public void validaSeChavesUnicasJaExistemParaNovoCliente(EmpresaEntity empresaLogada, ClienteRequest clienteRequest) {
        log.debug("Método de validação de chave única de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null)
            validaSeCpfCnpjJaExiste(clienteRequest.getCpfCnpj(), empresaLogada.getId());
    }

    public void validaSeChavesUnicasJaExistemParaClienteAtualizado(ClienteRequest clienteRequest,
                                                                   ClienteEntity clienteEntity,
                                                                   EmpresaEntity empresaLogada) {
        log.debug("Método de validação de chave única para atualização de cliente acessado...");
        if (clienteRequest.getCpfCnpj() != null && clienteEntity.getCpfCnpj() == null
                || clienteRequest.getCpfCnpj() != null
                && !clienteEntity.getCpfCnpj().equals(clienteRequest.getCpfCnpj()))
            validaSeCpfCnpjJaExiste(clienteRequest.getCpfCnpj(), empresaLogada.getId());
    }

}
