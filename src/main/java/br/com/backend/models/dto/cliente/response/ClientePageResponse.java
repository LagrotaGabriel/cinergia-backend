package br.com.backend.models.dto.cliente.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientePageResponse {
    List<ClienteResponse> content;
    Integer numberOfElements;
    Integer pageNumber;
    Integer pageSize;
    Integer size;
    Long totalElements;
    Integer totalPages;
}
