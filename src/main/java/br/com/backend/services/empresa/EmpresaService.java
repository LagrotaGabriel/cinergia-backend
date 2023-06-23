package br.com.backend.services.empresa;

import br.com.backend.models.dto.empresa.request.EmpresaRequest;
import br.com.backend.models.dto.empresa.response.EmpresaResponse;
import br.com.backend.models.entities.AcessoSistemaEntity;
import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.models.entities.empresa.EmpresaEntity;
import br.com.backend.models.enums.PerfilEnum;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import br.com.backend.services.pagamento.PagamentoService;
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
    PagamentoService pagamentoService;

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
                .saldo(0.0)
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

    public void adicionaSaldoContaEmpresa(PagamentoEntity pagamento) {
        log.debug("Método responsável por adicionar saldo à empresa após pagamento realizado acessado");

        log.debug("Iniciando acesso ao método de implementação de busca por id da empresa...");
        EmpresaEntity empresaEntity = empresaRepositoryImpl.implementaBuscaPorId(pagamento.getIdEmpresaResponsavel());

        log.debug("Setando saldo através do método de cálculo de valor líquido do pagamento...");
        empresaEntity.setSaldo(empresaEntity.getSaldo() + pagamentoService.calculaValorLiquidoPagamento(pagamento));

        log.debug("Iniciando persistência da empresa com o saldo atualizado...");
        empresaRepositoryImpl.implementaPersistencia(empresaEntity);
    }

}
