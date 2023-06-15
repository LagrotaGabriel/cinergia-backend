package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.entities.empresa.ContaEmpresaAsaasEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.proxy.empresa.response.SubContaAsaasResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmpresaTypeConverter {

    public EmpresaResponse converteEmpresaEntityParaEmpresaResponse(EmpresaEntity empresa) {
        log.debug("Método de conversão de objeto do tipo EmpresaEntity para objeto do tipo EmpresaResponse acessado");

        log.debug("Iniciando construção do objeto EmpresaResponse...");
        EmpresaResponse empresaResponse = EmpresaResponse.builder()
                .id(empresa.getId())
                .dataCadastro(empresa.getDataCadastro())
                .horaCadastro(empresa.getHoraCadastro())
                .nomeEmpresa(empresa.getNomeEmpresa())
                .email(empresa.getEmail())
                .cpfCnpj(empresa.getCpfCnpj())
                .dataNascimento(empresa.getDataNascimento())
                .endereco(empresa.getEndereco())
                .telefone(empresa.getTelefone())
                .build();
        log.debug("Objeto EmpresaResponse buildado com sucesso. Retornando...");
        return empresaResponse;
    }

    public ContaEmpresaAsaasEntity converteSubContaAsaasResponseParaContaEmpresaAsaasEntity(SubContaAsaasResponse subContaAsaasResponse) {
        log.debug("Método de conversão de objeto do tipo SubContaAsaasResponse para objeto do tipo ContaEmpresaAsaasEntity acessado");

        log.debug("Iniciando construção do objeto ContaEmpresaAsaasEntity...");
        ContaEmpresaAsaasEntity contaEmpresaAsaasEntity = ContaEmpresaAsaasEntity.builder()
                .accountId(subContaAsaasResponse.getId())
                .walletId(subContaAsaasResponse.getWalletId())
                .asaasApiKey(System.getenv("ACTIVE_PROFILE").equals("dev")
                        ? "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNTcxMzk6OiRhYWNoX2E1MmUxODI2LTQ0YmItNDc2MS1hNGYzLWNjMjA0YzljZTA5Nw=="
                        : subContaAsaasResponse.getApiKey())
                .agencia(subContaAsaasResponse.getAccountNumber().getAgency())
                .numeroConta(subContaAsaasResponse.getAccountNumber().getAccount())
                .digitoConta(subContaAsaasResponse.getAccountNumber().getAccountDigit())
                .build();
        log.debug("Objeto ContaEmpresaAsaasEntity buildado com sucesso. Retornando...");
        return contaEmpresaAsaasEntity;
    }
}
