package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.empresa.ContaEmpresaAsaasEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.entities.global.EnderecoEntity;
import br.com.backend.models.enums.PerfilEnum;
import br.com.backend.proxy.AsaasProxy;
import br.com.backend.proxy.empresa.request.SubContaAsaasRequest;
import br.com.backend.proxy.empresa.response.SubContaAsaasResponse;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import br.com.backend.services.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    AsaasProxy asaasProxy;

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
                .contaEmpresaAsaas(realizaCriacaoSubContaAsaas(empresaRequest))
                .nomeEmpresa(empresaRequest.getNomeEmpresa())
                .email(empresaRequest.getEmail())
                .cpfCnpj(empresaRequest.getCpfCnpj()
                        .replace("-", "")
                        .replace("/", "")
                        .replace(".", "")
                        .trim())
                .dataNascimento(empresaRequest.getDataNascimento())
                .endereco(empresaRequest.getEndereco())
                .telefone(empresaRequest.getTelefone())
                .celular(empresaRequest.getCelular())
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

    private ContaEmpresaAsaasEntity realizaCriacaoSubContaAsaas(EmpresaRequest empresaRequest) {

        log.debug("Método de serviço responsável pela criação de subconta da empresa na integradora ASAAS acessado");

        String telefone = empresaRequest.getTelefone().getPrefixo() + empresaRequest.getTelefone().getNumero();
        String celular = empresaRequest.getCelular().getPrefixo() + empresaRequest.getCelular().getNumero();
        EnderecoEntity endereco = empresaRequest.getEndereco();

        log.debug("Iniciando construção do objeto subContaAsaasRequest...");
        SubContaAsaasRequest subContaAsaasRequest = SubContaAsaasRequest.builder()
                .name(empresaRequest.getNomeEmpresa())
                .email(empresaRequest.getEmail())
                .cpfCnpj(empresaRequest.getCpfCnpj())
                .birthDate(empresaRequest.getDataNascimento())
                .phone(telefone)
                .mobilePhone(celular)
                .address(endereco.getLogradouro())
                .addressNumber(endereco.getNumero().toString())
                .complement(endereco.getComplemento())
                .province(endereco.getBairro())
                .postalCode(endereco.getCodigoPostal())
                .build();

        log.debug("Realizando envio de requisição de criação de subconta de empresa para a integradora ASAAS...");
        ResponseEntity<SubContaAsaasResponse> responseAsaas =
                asaasProxy.cadastraNovaSubConta(subContaAsaasRequest, System.getenv("TOKEN_ASAAS"));

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da subconta da empresa na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException("Ocorreu um erro no processo de criação da subconta da empresa: "
                    + responseAsaas.getBody());
        }
        log.debug("Criação da subconta ASAAS realizada com sucesso");

        SubContaAsaasResponse subContaAsaasResponse = responseAsaas.getBody();

        if (subContaAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da subconta é nulo");
            throw new InvalidRequestException("O retorno da integradora é nulo");
        }

        log.debug("Iniciando acesso ao método de conversão de objeto SubContaAsaasResponse para ContaEmpresaAsaasEntity...");
        return empresaTypeConverter.converteSubContaAsaasResponseParaContaEmpresaAsaasEntity(subContaAsaasResponse);
    }

}
