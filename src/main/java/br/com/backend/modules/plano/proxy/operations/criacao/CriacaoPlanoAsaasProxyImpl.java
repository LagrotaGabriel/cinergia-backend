package br.com.backend.modules.plano.proxy.operations.criacao;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.plano.models.dto.request.PlanoRequest;
import br.com.backend.modules.plano.proxy.PlanoAsaasProxy;
import br.com.backend.modules.plano.proxy.operations.criacao.models.request.CriaPlanoAsaasRequest;
import br.com.backend.modules.plano.proxy.operations.criacao.models.response.CriaPlanoAsaasResponse;
import br.com.backend.modules.plano.proxy.utils.PlanoAsaasUtil;
import br.com.backend.modules.plano.utils.ConstantesPlano;
import br.com.backend.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class CriacaoPlanoAsaasProxyImpl {

    @Autowired
    PlanoAsaasProxy planoAsaasProxy;

    @Autowired
    PlanoAsaasUtil planoAsaasUtil;

    public String realizaCriacaoDePlanoDeAssinaturaNaIntegradoraAsaas(PlanoRequest planoRequest,
                                                                      String asaasIdCliente) {

        log.debug("Método de serviço responsável pela criação de assinatura na integradora ASAAS acessado");

        log.debug("Iniciando construção do objeto CriaPlanoAsaasRequest...");
        CriaPlanoAsaasRequest criaPlanoAsaasRequest = CriaPlanoAsaasRequest.builder()
                .customer(asaasIdCliente)
                .billingType(planoRequest.getFormaPagamento())
                .value(planoRequest.getValor())
                .nextDueDate(String.valueOf(LocalDate.parse(planoRequest.getDataInicio())))
                .discount(null)
                .interest(null)
                .fine(null)
                .cycle(planoAsaasUtil.transformaPeriodicidadeEnumEmCycleEnum(planoRequest.getPeriodicidade()))
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
                    planoAsaasProxy.cadastraNovaAssinatura(criaPlanoAsaasRequest, System.getenv(Constantes.TOKEN_ASAAS));
        } catch (Exception e) {
            log.error(ConstantesPlano.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
            throw new InvalidRequestException(ConstantesPlano.ERRO_CRIACAO_ASSINATURA_ASAAS
                    + e.getMessage());
        }

        if (responseAsaas == null) {
            log.error("O valor retornado pela integradora na criação da assinatura é nulo");
            throw new InvalidRequestException(Constantes.RETORNO_INTEGRADORA_NULO);
        }

        if (responseAsaas.getStatusCodeValue() != 200) {
            log.error("Ocorreu um erro no processo de criação da assinatura na integradora de pagamentos: {}",
                    responseAsaas.getBody());
            throw new InvalidRequestException(ConstantesPlano.ERRO_CRIACAO_ASSINATURA_ASAAS
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
}
