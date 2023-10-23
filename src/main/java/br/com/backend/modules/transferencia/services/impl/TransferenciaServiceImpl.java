package br.com.backend.modules.transferencia.services.impl;

import br.com.backend.exceptions.custom.InternalErrorException;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.empresa.repository.impl.EmpresaRepositoryImpl;
import br.com.backend.modules.empresa.services.EmpresaService;
import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.dto.response.TransferenciaResponse;
import br.com.backend.modules.transferencia.models.dto.response.page.TransferenciaPageResponse;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.proxy.operations.cancelamento.CancelamentoTransferenciaAsaasProxyImpl;
import br.com.backend.modules.transferencia.proxy.operations.criacao.CriacaoTransferenciaAsaasProxyImpl;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.modules.transferencia.services.TransferenciaService;
import br.com.backend.modules.transferencia.services.utils.TransferenciaServiceUtil;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    @Autowired
    TransferenciaServiceUtil transferenciaServiceUtil;

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    CriacaoTransferenciaAsaasProxyImpl criacaoTransferenciaAsaasProxy;

    @Autowired
    CancelamentoTransferenciaAsaasProxyImpl cancelamentoTransferenciaAsaasProxy;

    public TransferenciaResponse criaTransferencia(UUID uuidEmpresaSessao,
                                                   TransferenciaRequest transferenciaRequest) {

        log.info("Método de serviço de criação de nova transferência acessado");

        log.info("Realizando a busca da empresa da sessão atual pelo id do principal do token: {}", uuidEmpresaSessao);
        EmpresaEntity empresaLogada = empresaRepositoryImpl.realizaBuscaDeEmpresaLogadaPorUuid(uuidEmpresaSessao);
        log.info("Busca da empresa realizada com sucesso");

        log.info("Inicia acesso ao método de tratamento da chave pix...");
        transferenciaRequest.setChavePix(transferenciaServiceUtil
                .realizaTratamentoChavePix(transferenciaRequest.getChavePix()));
        log.info("Tratamento da chave pix realizado com sucesso");

        log.info("Inicia acesso ao método de setagem de chave PIX EVP para ambiente de desenvolvimento...");
        transferenciaServiceUtil.realizaSetagemDeChaveEvpParaAmbienteDeDesenvolvimento(transferenciaRequest);

        log.info("Iniciando acesso ao método de implementação da criação da transferência na integradora ASAAS...");
        String idTransferenciaAsaas = criacaoTransferenciaAsaasProxy
                .realizaCriacaoTransferenciaAsaas(transferenciaRequest);
        log.info("Criação da transferência na integradora ASAAS realizada com sucesso");

        try {

            log.info("Iniciando acesso ao método de atualização do saldo da empresa logada...");
            empresaService.realizaAtualizacaoSaldoEmpresaParaTransferencia(empresaLogada, transferenciaRequest);
            log.info("Método de atualização de saldo da empresa executado e finalizado com sucesso");

            log.info("Iniciando criação do objeto TransferenciaEntity...");
            TransferenciaEntity transferenciaEntity = new TransferenciaEntity()
                    .constroiTransferenciaEntityParaCriacao(empresaLogada, idTransferenciaAsaas, transferenciaRequest);
            log.info("Objeto transferenciaEntity criado com sucesso");

            log.info("Realizando persistência da transferência...");
            TransferenciaEntity transferenciaPersistida = transferenciaRepository.save(transferenciaEntity);
            log.info("Transferência criada com sucesso");

            log.info("Iniciando conversão de transferência Entity para Transferencia Response...");
            TransferenciaResponse transferenciaResponse = new TransferenciaResponse()
                    .constroiTransferenciaResponse(transferenciaPersistida);
            log.info("Conversão de transferência para o tipo response realizada com sucesso. Retornando valores...");
            return transferenciaResponse;
        } catch (Exception e) {

            log.error("Ocorreu um erro durante o processo de persistência da transferência: {}", e.getMessage());

            log.info("Iniciando acesso ao método de cancelamento da transferência na integradora ASAAS " +
                    "para realização de rollback...");
            cancelamentoTransferenciaAsaasProxy.realizaCancelamentoDeTransferenciaNaIntegradoraAsaas(idTransferenciaAsaas);

            log.info("Rollback da transferência na integradora ASAAS finalizado com sucesso");
            throw new InternalErrorException("Ocorreu um erro durante a tentativa de persistência da transferÊncia. " +
                    "Erro: " + e.getMessage());
        }
    }

    public TransferenciaPageResponse obtemTransferenciasPaginadas(UUID uuidEmpresaSessao,
                                                                  Pageable pageable) {

        log.info("Método de serviço de obtenção paginada de transferências paginadas acessado");

        log.info("Acessando repositório de busca de transferências paginadas da empresa");
        Page<TransferenciaEntity> transferenciaPage = transferenciaRepository
                .buscaPorTransferenciasPaginadas(pageable, uuidEmpresaSessao);
        log.info("Busca por transferências paginadas realizada com sucesso");

        log.info("Busca de transferências por paginação realizada com sucesso. Acessando método de conversão " +
                "dos objetos do tipo Entity para objetos do tipo Response...");
        TransferenciaPageResponse transferenciaPageResponse = new TransferenciaPageResponse()
                .constroiObjeto(transferenciaPage);
        log.info(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de transferências foi realizada com sucesso");
        return transferenciaPageResponse;

    }

}
