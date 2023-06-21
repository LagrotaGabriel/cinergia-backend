package br.com.backend.repositories.pagamento.impl;

import br.com.backend.models.entities.PagamentoEntity;
import br.com.backend.repositories.pagamento.PagamentoRepository;
import br.com.backend.services.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PagamentoRepositoryImpl {

    @Autowired
    PagamentoRepository pagamentoRepository;

    public PagamentoEntity implementaBuscaPorCodigoPagamentoAsaas(String codigoAsaas) {
        log.debug("Método que implementa busca por pagamento Asaas pelo seu código de pagamento acessado");

        Optional<PagamentoEntity> pagamentoOptional =
                pagamentoRepository.findByIdAsaas(codigoAsaas);

        PagamentoEntity pagamentoEntity;
        if (pagamentoOptional.isPresent()) {
            pagamentoEntity = pagamentoOptional.get();
            log.debug("Pagamento encontrado: {}", pagamentoEntity);
        } else {
            log.warn("Nenhum pagamento foi encontrado com o código Asaas informado: {}", codigoAsaas);
            throw new ObjectNotFoundException("Nenhum pagamento foi encontrado com o codigo Asaas informado");
        }

        log.debug("Retornando pagamento...");
        return pagamentoEntity;
    }

}
