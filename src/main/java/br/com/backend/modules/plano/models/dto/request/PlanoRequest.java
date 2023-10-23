package br.com.backend.modules.plano.models.dto.request;

import br.com.backend.globals.enums.FormaPagamentoEnum;
import br.com.backend.modules.plano.models.enums.NotificacaoEnum;
import br.com.backend.modules.plano.models.enums.PeriodicidadeEnum;
import lombok.*;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoRequest {
    @NotEmpty(message = "A data de início do plano não pode ser nula")
    @Pattern(regexp = "^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12]\\d|3[01])$",
            message = "O padrão do campo data de início está inválido")
    private String dataInicio;

    @NotEmpty(message = "A descrição do plano não pode estar vazia")
    @Size(max = 70, message = "A descrição do plano deve conter no máximo {max} caracteres")
    private String descricao;

    @NotNull(message = "O valor do plano não pode estar vazio")
    @Min(value = 1, message = "O valor do plano não pode ser menor de R$ 1,00")
    @Max(value = 100000, message = "O valor do plano não pode ser maior de R$ 100.000,00")
    private Double valor;

    @NotNull(message = "A forma de pagamento do plano não pode ser nula")
    private FormaPagamentoEnum formaPagamento;

    @NotNull(message = "A periodicidade do pagamento não pode ser nula")
    private PeriodicidadeEnum periodicidade;

    @Builder.Default
    private Set<NotificacaoEnum> notificacoes = new HashSet<>();
}
