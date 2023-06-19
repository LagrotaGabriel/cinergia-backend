package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.entities.empresa.EmpresaEntity;
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

}
