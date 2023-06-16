package br.com.backend.models.dto.plano.response;

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
