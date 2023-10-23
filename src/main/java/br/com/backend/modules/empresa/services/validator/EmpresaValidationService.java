package br.com.backend.modules.empresa.services.validator;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.empresa.models.dto.request.EmpresaRequest;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmpresaValidationService {

    @Autowired
    EmpresaRepository empresaRepository;

    public void validaSeCpfCnpjJaExiste(String cpfCnpj) {
        log.debug("Método de validação de chave única de CPF/CNPJ acessado");

        if (Boolean.TRUE.equals(empresaRepository.existsByCpfCnpj(cpfCnpj))) {
            String mensagemErro = cpfCnpj.length() == 14
                    ? "O CPF informado já existe"
                    : "O CNPJ informado já existe";
            log.warn(mensagemErro + ": {}", cpfCnpj);
            throw new InvalidRequestException(mensagemErro);
        }
        log.debug("Validação de chave única de CPF/CNPJ... OK");
    }

    public void validaSeEmailJaExiste(String email) {
        log.debug("Método de validação de chave única de e-mail acessado");

        if (Boolean.TRUE.equals(empresaRepository.existsByEmail(email))) {
            log.warn("O e-mail informado já existe: {}", email);
            throw new InvalidRequestException("O e-mail informado já existe");
        }
        log.debug("Validação de chave única de e-mail... OK");
    }

    public void validaSeChavesUnicasJaExistemParaNovaEmpresa(EmpresaRequest empresaRequest) {
        log.debug("Método de validação de chave única de empresa acessado...");
        if (empresaRequest.getCpfCnpj() != null)
            validaSeCpfCnpjJaExiste(empresaRequest.getCpfCnpj());
        if (empresaRequest.getEmail() != null)
            validaSeEmailJaExiste(empresaRequest.getEmail());
    }

    public void validaSeEmpresaPossuiSaldoSuficienteParaRealizarTransferencia(EmpresaEntity empresaLogada,
                                                                              Double valorTransferencia) {
        log.info("Método responsável por realizar validação se empresa possui saldo suficiente para " +
                "realizar transferência acessado");

        log.info("Iniciando validação...");
        if (valorTransferencia > empresaLogada.getSaldo()) {
            log.warn("A empresa não possui saldo suficiente para realizar a transferência desejada");
            throw new InvalidRequestException("Você não possui saldo suficiente para realizar essa transferência");
        }
        log.info("O saldo da empresa é suficiente para realizar a transferência");
    }
}
