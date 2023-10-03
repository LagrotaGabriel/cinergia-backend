package br.com.backend.modules.pagamento.repository.impl;

import br.com.backend.exceptions.custom.ObjectNotFoundException;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import br.com.backend.modules.pagamento.repository.PagamentoRepository;
import br.com.backend.modules.pagamento.utils.ConstantesPagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class PagamentoRepositoryImpl {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Transactional
    public PagamentoEntity implementaPersistencia(PagamentoEntity pagamento) {
        log.debug("Método de serviço que implementa persistência do pagamento acessado");
        PagamentoEntity pagamentoEntity = pagamentoRepository.save(pagamento);
        log.debug("Persistência do objeto pagamento realizada com sucesso");
        return pagamentoEntity;
    }

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

    public PagamentoEntity implementaBuscaPorId(Long id, Long idEmpresa) {
        log.debug("Método que implementa busca de pagamento por id acessado. Id: {}", id);

        Optional<PagamentoEntity> pagamentoOptional = pagamentoRepository.buscaPorId(id, idEmpresa);

        PagamentoEntity pagamentoEntity;
        if (pagamentoOptional.isPresent()) {
            pagamentoEntity = pagamentoOptional.get();
            log.debug(ConstantesPagamento.PAGAMENTO_ENCONTRADO, pagamentoEntity);
        } else {
            log.warn("Nenhum pagamento foi encontrado com o id {}", id);
            throw new ObjectNotFoundException("Nenhum pagamento foi encontrado com o id informado");
        }
        log.debug("Retornando o pagamento encontrado...");
        return pagamentoEntity;
    }

}
