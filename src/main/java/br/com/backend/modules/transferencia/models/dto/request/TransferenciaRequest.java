package br.com.backend.modules.transferencia.models.dto.request;

import br.com.backend.modules.transferencia.models.enums.TipoChavePixEnum;
import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequest {

    @NotEmpty(message = "A descrição da transferência não pode estar vazia")
    @Size(max = 70, message = "A descrição da transferência deve conter no máximo {max} caracteres")
    private String descricao;

    @NotNull(message = "O valor da transferência não pode estar vazio")
    @Min(value = 1, message = "O valor da transferência não pode ser menor de R$ 1,00")
    @Max(value = 100000, message = "O valor da transferência não pode ser maior de R$ 100.000,00")
    private Double valor;

    @NotEmpty(message = "A chave pix da transferência não pode estar vazia")
    @Size(max = 70, message = "A chave pix transferência deve conter no máximo {max} caracteres")
    private String chavePix;

    @NotNull(message = "O tipo da chave pix não pode ser nulo")
    private TipoChavePixEnum tipoChavePix;
}