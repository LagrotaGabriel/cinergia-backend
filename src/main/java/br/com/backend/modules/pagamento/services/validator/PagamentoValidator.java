package br.com.backend.modules.pagamento.services.validator;

import br.com.backend.exceptions.custom.InvalidRequestException;
import br.com.backend.modules.pagamento.models.entity.PagamentoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PagamentoValidator {

    public void realizaValidacoesCancelamento(PagamentoEntity pagamentoEntity) {
        switch (pagamentoEntity.getStatusPagamento()) {
            case CANCELADO -> throw new InvalidRequestException("Não é possível remover um pagamento que já foi removido");
            case APROVADO -> throw new InvalidRequestException("Não é possível remover um pagamento que já foi realizado");
            case REPROVADO -> throw new InvalidRequestException("Não é possível remover um pagamento recusado");
            default -> log.info("Validação de pagamentos realizada com sucesso");
        }
    }
}
