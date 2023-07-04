package br.com.backend.proxy.transferencia.response;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeAsaas {
    private String encodedImage;
    private String payload;
}
