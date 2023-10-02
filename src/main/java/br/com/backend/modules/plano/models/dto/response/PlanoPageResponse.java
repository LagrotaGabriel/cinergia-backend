package br.com.backend.modules.plano.models.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlanoPageResponse {
    List<PlanoResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;
}
