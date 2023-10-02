package br.com.backend.modules.plano.proxy.models.response.consultar;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaAssinaturaResponse {
    private String id;
    private String dateCreated;
    private String customer;
    private String paymentLink;
    private String billingType;
    private Double value;
    private String nextDueDate;
    private String cycle;
    private String description;
    private String endDate;
    private String maxPayments;
    private String status;
    private String externalReference;
    private List<ConsultaSplitResponse> split;
}
