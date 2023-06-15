package br.com.backend.proxy.empresa.response;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaSubContaResponse {
    private String object;
    private Boolean hasMore;
    private Integer totalCount;
    private Integer limit;
    private Integer offSet;
    private List<SubContaAsaasResponse> data;

}
