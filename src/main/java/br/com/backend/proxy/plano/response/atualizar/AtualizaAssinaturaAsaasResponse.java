package br.com.backend.proxy.plano.response.atualizar;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaAssinaturaAsaasResponse {
    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String paymentLink;
    private String billingType;
    private String cycle;
    private Double value;
    private String nextDueDate;
    private String description;
    private String status;
    private AtualizaAssinaturaDiscountResponse discount;
    private AtualizaAssinaturaFineResponse fine;
    private AtualizaAssinaturaInterestResponse interest;
    private Boolean deleted;
}