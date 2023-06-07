package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.models.enums.PerfilEnum;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class EmpresaService {

    @Autowired
    EmpresaValidationService empresaValidationService;

    @Autowired
    EmpresaTypeConverter empresaTypeConverter;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public EmpresaResponse criaNovaEmpresa(EmpresaRequest empresaRequest) {

        log.debug("Método de serviço responsável pelo tratamento do objeto recebido e criação de nova empresa acessado");

        log.debug("Iniciando acesso ao método de validação de variáveis de chave única para empresa...");
        empresaValidationService.validaSeChavesUnicasJaExistemParaNovaEmpresa(empresaRequest);

        log.debug("Criando hashset com perfil empresa...");
        Set<PerfilEnum> perfis = new HashSet<>();
        perfis.add(PerfilEnum.EMPRESA);

        log.debug("Iniciando construção do objeto EmpresaEntity...");
        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .nomeEmpresa(empresaRequest.getNomeEmpresa())
                .email(empresaRequest.getEmail())
                .cpfCnpj(empresaRequest.getCpfCnpj()
                        .replace("-", "")
                        .replace("/", "")
                        .replace(".", "")
                        .trim())
                .tipoPessoaEnum(empresaRequest.getTipoPessoaEnum())
                .endereco(empresaRequest.getEndereco())
                .telefone(empresaRequest.getTelefone())
                .acessoSistema(AcessoSistemaEntity.builder()
                        .senha(new BCryptPasswordEncoder().encode(empresaRequest.getAcessoSistema().getSenha()))
                        .perfis(perfis)
                        .build())
                .logoEmpresa(null)
                .modelosPlano(new ArrayList<>())
                .clientes(new ArrayList<>())
                .build();
        log.debug("Construção de objeto EmpresaEntity realizado com sucesso");

        log.debug("Iniciando acesso ao método de implementação de persistência do cliente...");
        EmpresaEntity empresaPersistida = empresaRepositoryImpl.implementaPersistencia(empresaEntity);

        log.info("Empresa criada com sucesso");
        return empresaTypeConverter.converteEmpresaEntityParaEmpresaResponse(empresaPersistida);
    }

}
