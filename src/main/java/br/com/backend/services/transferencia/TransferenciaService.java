package br.com.backend.services.transferencia;

import br.com.backend.models.dto.transferencia.request.TransferenciaRequest;
import br.com.backend.models.dto.transferencia.response.TransferenciaPageResponse;
import br.com.backend.models.entities.EmpresaEntity;
import br.com.backend.models.entities.TransferenciaEntity;
import br.com.backend.models.enums.StatusTransferenciaEnum;
import br.com.backend.models.enums.TipoChavePixEnum;
import br.com.backend.proxy.AsaasProxy;
import br.com.backend.proxy.transferencia.request.CriaChavePixAsaasRequest;
import br.com.backend.proxy.transferencia.request.TransferePixAsaasRequest;
import br.com.backend.proxy.transferencia.response.CriaChavePixAsaasResponse;
import br.com.backend.proxy.transferencia.response.TransferePixAsaasResponse;
import br.com.backend.proxy.webhooks.transferencia.AtualizacaoTransferenciaWebHook;
import br.com.backend.repositories.empresa.impl.EmpresaRepositoryImpl;
import br.com.backend.repositories.transferencia.TransferenciaRepository;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
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
    AsaasProxy asaasProxy;

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
            transferenciaRequest.setChavePix(criaChavePixAsaasParaTestes());
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
                    asaasProxy.transferirPix(transferePixAsaasRequest, System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CRIACAO_TRANSFERENCIA_ASAAS
                    + e.getMessage());
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

    private String criaChavePixAsaasParaTestes() {

        log.debug("Método de serviço responsável pela criação de chave PIX aleatória de testes na integradora ASAAS acessado");


        ResponseEntity<CriaChavePixAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de criação de transferência para a integradora ASAAS...");
            responseAsaas = asaasProxy.criaChavePix(
                    new CriaChavePixAsaasRequest(TipoChavePixEnum.EVP),
                    System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CRIACAO_CHAVE_ALEATORIA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_CHAVE_ALEATORIA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da chave PIX é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da chave PIX na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_CHAVE_ALEATORIA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Criação de chave PIX ASAAS realizada com sucesso");

        CriaChavePixAsaasResponse criaChavePixAsaasResponse = responseAsaas.getBody();

        if (criaChavePixAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da chave PIX é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.debug("Retornando chave pix gerada: {}", criaChavePixAsaasResponse.getKey());
        return criaChavePixAsaasResponse.getKey();
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
        log.debug("Método 'motor de distribuição' de Webhook de atualização de transferência acessado");
        System.err.println(atualizacaoTransferenciaWebHook);
    }

}
