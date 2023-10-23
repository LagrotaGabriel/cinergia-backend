package br.com.backend.globals.models.imagem.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImagemResponse {
    private String nome;
    private Long tamanho;
    private String tipo;
    private byte[] arquivo;

}
