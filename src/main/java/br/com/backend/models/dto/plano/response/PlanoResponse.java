package br.com.backend.models.dto.plano.response;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {
    private Long id;
    private String dataCadastro;
    private String horaCadastro;
    private String dataVencimento;
    private String dataInicio;
    private String descricao;
    private Double valor;
    private String formaPagamento;
    private String statusPlano;
    private String periodicidade;
    protected List<String> notificacoes;
}
