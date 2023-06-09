package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import br.com.backend.services.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmpresaValidationService {

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public void validaSeCpfCnpjJaExiste(String cpfCnpj) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");
        if (empresaRepositoryImpl.implementaBuscaPorCpfCnpjIdentico(cpfCnpj).isPresent()) {
            String mensagemErro = cpfCnpj.length() == 11 ? "O CPF informado já existe" : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public void validaSeChavesUnicasJaExistemParaNovaEmpresa(EmpresaRequest empresaRequest) {
        log.debug("Método de validação de chave única de empresa acessado...");
        if (empresaRequest.getCpfCnpj() != null)
            validaSeCpfCnpjJaExiste(empresaRequest.getCpfCnpj());
    }
}
