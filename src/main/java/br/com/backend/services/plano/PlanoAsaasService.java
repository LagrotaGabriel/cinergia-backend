package br.com.backend.services.plano;

import br.com.backend.models.dto.plano.request.PlanoRequest;
import br.com.backend.models.entities.ClienteEntity;
import br.com.backend.models.entities.PlanoEntity;
import br.com.backend.proxy.AsaasProxy;
import br.com.backend.proxy.plano.request.criar.CriaPlanoAsaasRequest;
import br.com.backend.proxy.plano.response.criar.CriaPlanoAsaasResponse;
import br.com.backend.proxy.plano.response.cancelar.CancelamentoAssinaturaResponse;
import br.com.backend.proxy.plano.response.consultar.ConsultaAssinaturaResponse;
import br.com.backend.services.exceptions.FeignConnectionException;
import br.com.backend.services.exceptions.InvalidRequestException;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlanoAsaasService {

    @Autowired
    AsaasProxy asaasProxy;

    @Autowired
    PlanoTypeConverter planoTypeConverter;

    protected String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
                                                                       ClienteEntity clienteEntity) {

        log.debug("Método de serviço responsável pela criação de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto CriaPlanoAsaasRequest...");
        CriaPlanoAsaasRequest criaPlanoAsaasRequest = CriaPlanoAsaasRequest.builder()
                .customer(clienteEntity.getAsaasId())
                .billingType(planoRequest.getFormaPagamento())
                .value(planoRequest.getValor())
                .nextDueDate(planoRequest.getDataInicio())
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(planoTypeConverter.transformaPeriodicidadeEnumEmCycleEnum(planoRequest.getPeriodicidade()))
                .description(planoRequest.getDescricao())
                .endDate(null)
                .maxPayments(null)
                .externalReference(null)
                .split(null)
                .build();

        ResponseEntity<CriaPlanoAsaasResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de criação de assinatura para a integradora ASAAS...");
            responseAsaas =
                    asaasProxy.cadastraNovaAssinatura(criaPlanoAsaasRequest, System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Criação de assinatura ASAAS realizada com sucesso");

        CriaPlanoAsaasResponse planoAsaasResponse = responseAsaas.getBody();

        if (planoAsaasResponse == null) {
            log.error("O valor retornado pela integradora na criação da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        log.debug("Retornando id da assinatura gerado: {}", planoAsaasResponse.getId());
        return planoAsaasResponse.getId();
    }

    protected void realizaCancelamentoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoEntity planoEntity) {

        log.debug("Método de serviço responsável pela cancelamento de assinatura na integradora ASAAS acessado");
        ResponseEntity<CancelamentoAssinaturaResponse> responseAsaas;

        try {
            log.debug("Realizando envio de requisição de cancelamento de assinatura para a integradora ASAAS...");
            responseAsaas =
                    asaasProxy.cancelarAssinatura(planoEntity.getIdAsaas(), System.getenv("TOKEN_ASAAS"));
        } catch (Exception e) {
            log.error(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de cancelamento da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(Constantes.ERRO_CANCELAMENTO_ASSINATURA_ASAAS
                    + responseAsaas.getBody());
        }
        log.debug("Cancelamento de assinatura ASAAS realizada com sucesso");

        CancelamentoAssinaturaResponse cancelamentoAssinaturaResponse = responseAsaas.getBody();

        if (cancelamentoAssinaturaResponse == null) {
            log.error("O valor retornado pela integradora na cancelamento da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

    }

    public ConsultaAssinaturaResponse consultaAssinaturaAsaas(String id) {
        log.debug("Método de consulta de plano ASAAS por ID ({}) acessado", id);
        ResponseEntity<ConsultaAssinaturaResponse> assinaturaAsaas;
        try {
            assinaturaAsaas =
                    asaasProxy.consultaAssinatura(id, System.getenv(Constantes.TOKEN_ASAAS));
            log.debug("Plano encontrado: {}", assinaturaAsaas);
        } catch (Exception e) {
            log.error("Houve uma falha de comunicação com a integradora de pagamentos: {}", e.getMessage());
            throw new FeignConnectionException(Constantes.FALHA_COMUNICACAO_ASAAS + e.getMessage());
        }

        if (assinaturaAsaas.getStatusCodeValue() != 200) {
            log.error("Houve uma falha no processo de consulta do plano com a integradora de pagamentos: {}",
                    assinaturaAsaas.getBody());
            throw new InvalidRequestException("Ocorreu um erro no processo de consulta de plano com a integradora: "
                    + assinaturaAsaas.getBody());
        }

        log.debug("Retornando plano...");
        return assinaturaAsaas.getBody();
    }
}
