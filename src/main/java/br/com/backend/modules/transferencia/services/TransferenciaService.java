package br.com.backend.modules.transferencia.services;

import br.com.backend.modules.transferencia.models.dto.request.TransferenciaRequest;
import br.com.backend.modules.transferencia.models.dto.response.TransferenciaPageResponse;
import br.com.backend.modules.empresa.models.entity.EmpresaEntity;
import br.com.backend.modules.notificacao.models.entity.NotificacaoEntity;
import br.com.backend.modules.transferencia.models.entity.TransferenciaEntity;
import br.com.backend.modules.transferencia.models.enums.StatusTransferenciaEnum;
import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import br.com.backend.modules.notificacao.models.enums.TipoNotificacaoPlanoEnum;
import br.com.backend.modules.transferencia.proxy.TransferenciaAsaasProxy;
import br.com.backend.modules.transferencia.proxy.models.request.TransferePixAsaasRequest;
import br.com.backend.modules.transferencia.proxy.models.response.TransferePixAsaasResponse;
import br.com.backend.modules.transferencia.services.adapter.TransferenciaTypeConverter;
import br.com.backend.modules.transferencia.hook.models.AtualizacaoTransferenciaWebHook;
import br.com.backend.modules.empresa.repository.impl.EmpresaRepositoryImpl;
import br.com.backend.modules.transferencia.repository.TransferenciaRepository;
import br.com.backend.modules.transferencia.repository.impl.TransferenciaRepositoryImpl;
import br.com.backend.exceptions.FeignConnectionException;
import br.com.backend.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import br.com.backend.util.ConversorDeDados;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
public class TransferenciaService {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    TransferenciaRepositoryImpl transferenciaRepositoryImpl;

    @Autowired
    TransferenciaAsaasProxy transferenciaAsaasProxy;

    @Autowired
    TransferenciaTypeConverter transferenciaTypeConverter;

    @Autowired
    EmpresaRepositoryImpl empresaRepositoryImpl;

    public void criaTransferencia(EmpresaEntity empresa, TransferenciaRequest transferenciaRequest) {
        log.debug("Método de serviço de criação de nova transferência acessado");

        if (transferenciaRequest.getValor() > empresa.getSaldo())
            throw new InvalidRequestException("Você não possui saldo suficiente para realizar essa transferência");

        transferenciaRequest.setChavePix(transferenciaRequest.getChavePix()
                .replace(".", "")
                .replace("-", "")
                .replace("/", "")
                .replace(",", ""));

        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            transferenciaRequest.setChavePix("8512895b-99d1-4e3e-ac20-ba5e811f14b9");
            transferenciaRequest.setTipoChavePix(TipoChavePixEnum.EVP);
        }

        empresa.setSaldo(empresa.getSaldo() - transferenciaRequest.getValor());

        log.debug("Iniciando criação do objeto TransferenciaEntity...");
        TransferenciaEntity transferenciaEntity = TransferenciaEntity.builder()
                .idEmpresaResponsavel(empresa.getId())
                .asaasId(realizaCriacaoTransferenciaAsaas(transferenciaRequest))
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .valor(transferenciaRequest.getValor())
                .chavePix(transferenciaRequest.getChavePix())
                .tipoChavePix(transferenciaRequest.getTipoChavePix())
                .statusTransferencia(StatusTransferenciaEnum.PENDENTE)
                .descricao(transferenciaRequest.getDescricao())
                .build();
        log.debug("Objeto transferenciaEntity criado com sucesso");

        log.debug("Inserindo objeto transferenciaEntity na empresa logada...");
        empresa.getTransferencias().add(transferenciaEntity);

        log.debug("Iniciando acesso ao método de implementação da persistência da empresa com a transferência acoplada...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
        log.debug("Empresa atualizada persistida com sucesso");

        log.info("Transferência criada com sucesso");
    }

    private String realizaCriacaoTransferenciaAsaas(TransferenciaRequest transferenciaRequest) {

        log.debug("Método de serviço responsável pela criação de transferência na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto transferePixAsaasRequest...");
        TransferePixAsaasRequest transferePixAsaasRequest = TransferePixAsaasRequest.builder()
                .value(transferenciaRequest.getValor())
                .pixAddressKey(transferenciaRequest.getChavePix())
                .pixAddressKeyType(transferenciaRequest.getTipoChavePix())
                .description(transferenciaRequest.getDescricao())
                .scheduleDate(null)
                .build();

        ResponseEntity<TransferePixAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de criação de transferência para a integradora ASAAS...");
            responseAsaas =
                    transferenciaAsaasProxy.transferirPix(transferePixAsaasRequest, System.getenv("TOKEN_ASAAS"));
        } catch (FeignException e) {
            log.error(Constantes.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                    + e.getMessage());
            if (e.status() == 409)
                throw new FeignConnectionException("Ocorreu um erro interno ao tentar realizar sua transferência. Tente novamente em 5 minutos");
            else
                throw new InvalidRequestException(Constantes.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                        + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da transferência na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Criação de transferência ASAAS realizada com sucesso");

        TransferePixAsaasResponse transferePixAsaasResponse = responseAsaas.getBody();

        if (transferePixAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da transferência é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.debug("Retornando id da transferência gerada: {}", transferePixAsaasResponse.getId());
        return transferePixAsaasResponse.getId();
    }

    public TransferenciaPageResponse obtemTransferenciasEmpresa(EmpresaEntity empresa, Pageable pageable) {

        log.debug("Método de serviço de obtenção paginada de transferências da empresa acessado");

        log.debug("Acessando repositório de busca de transferências da empresa");
        Page<TransferenciaEntity> transferenciaPage = transferenciaRepository.buscaPorTransferencias(pageable, empresa.getId());

        log.debug("Busca de transferências por paginação realizada com sucesso. Acessando método de conversão dos objetos do tipo " +
                "Entity para objetos do tipo Response...");
        TransferenciaPageResponse transferenciaPageResponse = transferenciaTypeConverter
                .converteListaDeTransferenciasEntityParaTransferenciasResponse(transferenciaPage);
        log.debug(Constantes.CONVERSAO_DE_TIPAGEM_COM_SUCESSO);

        log.info("A busca paginada de transferências foi realizada com sucesso");
        return transferenciaPageResponse;

    }

    public void realizaTratamentoWebhookTransferencia(AtualizacaoTransferenciaWebHook atualizacaoTransferenciaWebHook) {
        log.info("Método 'motor de distribuição' de Webhook de atualização de transferência acessado. " +
                "Objeto recebido: {}", atualizacaoTransferenciaWebHook);

        log.debug("Iniciando busca de transferência pelo código ASAAS...");
        TransferenciaEntity transferenciaEntity = transferenciaRepositoryImpl
                .implementaBuscaPorCodigoTransferenciaAsaas(atualizacaoTransferenciaWebHook.getTransfer().getId());

        log.debug("Iniciando busca da empresa responsável pela transferência pelo ID...");
        EmpresaEntity empresa = empresaRepositoryImpl.implementaBuscaPorId(transferenciaEntity.getId());

        log.debug("Motor de distribuição do status da transferência iniciado");
        switch (atualizacaoTransferenciaWebHook.getEvent()) {
            case "RECEIVED": {
                log.debug("Iniciando acesso ao método de tratamento de transferência realizada com sucesso...");
                tratamentoWebhookTransferenciaComSucesso(empresa, transferenciaEntity);
                break;
            }
            case "FAILED": {
                log.debug("Iniciando acesso ao método de tratamento de transferência realizada com erro...");
                tratamentoWebhookTransferenciaComErro(empresa, transferenciaEntity);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void tratamentoWebhookTransferenciaComSucesso(EmpresaEntity empresa, TransferenciaEntity transferenciaEntity) {

        log.debug("Método responsável pelo tratamento de webhooks com transferências realizadas com sucesso acessado");

        log.debug("Iniciando construção do objeto notificação para transferência realizada com sucesso");
        NotificacaoEntity notificacaoEntity = NotificacaoEntity.builder()
                .idEmpresaResponsavel(empresa.getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .descricao("Transferência PIX no valor de "
                        + ConversorDeDados.converteValorDoubleParaValorMonetario(transferenciaEntity.getValor())
                        + " realizada com sucesso")
                .uri(null)
                .tipoNotificacaoEnum(TipoNotificacaoPlanoEnum.TRANSFERENCIA_SUCESSO)
                .lida(false)
                .build();

        log.debug("Adicionando notificação ao objeto empresa...");
        empresa.getNotificacoes().add(notificacaoEntity);

        log.debug("Removendo transferência da empresa: {}", transferenciaEntity);
        empresa.getTransferencias().remove(transferenciaEntity);

        log.debug("Setando status da transferência como aprovado...");
        transferenciaEntity.setStatusTransferencia(StatusTransferenciaEnum.SUCESSO);

        log.debug("Adicionado transferência atualizada à lista de transferências da empresa...");
        empresa.getTransferencias().add(transferenciaEntity);

        log.debug("Iniciando persistência da empresa atualizada...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
    }

    public void tratamentoWebhookTransferenciaComErro(EmpresaEntity empresa, TransferenciaEntity transferenciaEntity) {

        log.debug("Método responsável pelo tratamento de webhooks com transferências realizadas com erro acessado");

        log.debug("Iniciando construção do objeto notificação para transferência realizada com sucesso");
        NotificacaoEntity notificacaoEntity = NotificacaoEntity.builder()
                .idEmpresaResponsavel(empresa.getId())
                .dataCadastro(LocalDate.now().toString())
                .horaCadastro(LocalTime.now().toString())
                .descricao("A transferência PIX no valor de "
                        + ConversorDeDados.converteValorDoubleParaValorMonetario(transferenciaEntity.getValor())
                        + " não pode ser realizada")
                .uri(null)
                .tipoNotificacaoEnum(TipoNotificacaoPlanoEnum.TRANSFERENCIA_ERRO)
                .lida(false)
                .build();

        log.debug("Adicionando notificação ao objeto empresa...");
        empresa.getNotificacoes().add(notificacaoEntity);

        log.debug("Removendo transferência da empresa: {}", transferenciaEntity);
        empresa.getTransferencias().remove(transferenciaEntity);

        log.debug("Setando status da transferência como recusado...");
        transferenciaEntity.setStatusTransferencia(StatusTransferenciaEnum.FALHA);

        log.debug("Adicionado transferência atualizada à lista de transferências da empresa...");
        empresa.getTransferencias().add(transferenciaEntity);

        log.debug("Iniciando persistência da empresa atualizada...");
        empresaRepositoryImpl.implementaPersistencia(empresa);
    }
}
