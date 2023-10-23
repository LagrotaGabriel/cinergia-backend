package br.com.backend.modules.transferencia.hook.models.bank;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private String code;
    private String name;
    private String ispb;
}
